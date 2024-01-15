import chisel3._

trait HasTestIO { this: Module =>
  val io = IO(new Bundle {
    val in0 = Input(UInt(16.W))
    val in1 = Input(UInt(16.W))
    val in2 = Input(UInt(16.W))
    val result = Output(UInt(16.W))
  })
  dontTouch(io)

  def calc: UInt = io.in0 + (io.in1.asSInt / io.in2.asSInt).asUInt
}

class Test1 extends Module with HasTestIO {
  val result = RegNext(calc)

  io.result := result
}


class Test2 extends Module with HasTestIO {
  val result = Reg(UInt(16.W))
  result := calc

  io.result := result
}

/**
  * Compute GCD using subtraction method.
  * Subtracts the smaller from the larger until register y is zero.
  * value in register x is then the GCD
  */
class GCD extends Module {
  val tests = Seq(Module(new Test1), Module(new Test2))
  tests.foreach(_.io.in0 := 0xFFFF.U)
  tests.foreach(_.io.in1 := 0xFFFF.U)
  tests.foreach(_.io.in2 := 0x0002.U)
  printf(p"result0: ${Hexadecimal(tests(0).io.result)}\n")
  printf(p"result1: ${Hexadecimal(tests(1).io.result)}\n")
  assert(tests(0).io.result === tests(1).io.result)
}
