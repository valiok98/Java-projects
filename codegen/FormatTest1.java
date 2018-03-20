package codegen;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FormatTest1 {

  @Test
  public void testProgramsSameLine()
  {
    String[] codesWithBraceOnSameLine = {
        //ggt
       /* "int ggt(int a, int b) {\n" +
            "  int temp;\n" +
            "  if (b > a) {\n" +
            "    temp = a;\n" +
            "    a = b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  while (a != 0) {\n" +
            "    temp = a;\n" +
            "    a = a % b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  return b;\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int a, b, r;\n" +
            "  a = 3528;\n" +
            "  b = 3780;\n" +
            "  r = ggt(a, b);\n" +
            "  return r;\n" +
            "}",*/
        //fak
        "int fak(int n) {\n" +
            "  if (n == 0) {\n" +
            "    return 1;\n" +
            "  }\n" +
            "  return n * fak(n - 1);\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  return fak(6);\n" +
            "}",
        //prim-test
        "int prim(int n) {\n" +
            "  int divisors, i;\n" +
            "  divisors = 0;\n" +
            "  i = 2;\n" +
            "  while (i < n) {\n" +
            "    if (n % i == 0) {\n" +
            "      divisors = divisors + 1;\n" +
            "    }\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  if (divisors == 0 && n >= 2) {\n" +
            "    return 1;\n" +
            "  } else {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main() {\n" +
            "  int prims;\n" +
            "  prims = 0;\n" +
            "  prims = prims + prim(997);\n" +
            "  prims = prims + prim(120);\n" +
            "  prims = prims + prim(887);\n" +
            "  prims = prims + prim(21);\n" +
            "  prims = prims + prim(379);\n" +
            "  prims = prims + prim(380);\n" +
            "  prims = prims + prim(757);\n" +
            "  prims = prims + prim(449);\n" +
            "  prims = prims + prim(5251);\n" +
            "  return prims;\n" +
            "}"
    };

    for(String code : codesWithBraceOnSameLine)
    {
      
      
      //Alternativ
    	ParsyMcParseface parser = new ParsyMcParseface(code);
      Program program = parser.liefert();
      FormatVisitor fv = new FormatVisitor();
      program.accept(fv);
      //System.out.println(fv.getResult());
      assertEquals(code, fv.getResult());
    }
  }

  @Test
  public void testProgramsNextLine()
  {
    String[] codesWithBraceOnNextLine = {
        /*//ggt
        "int ggt(int a, int b)\n{\n" +
            "  int temp;\n" +
            "  if (b > a)\n" +
            "  {\n" +
            "    temp = a;\n" +
            "    a = b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  while (a != 0)\n" +
            "  {\n" +
            "    temp = a;\n" +
            "    a = a % b;\n" +
            "    b = temp;\n" +
            "  }\n" +
            "  return b;\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  int a, b, r;\n" +
            "  a = 3528;\n" +
            "  b = 3780;\n" +
            "  r = ggt(a, b);\n" +
            "  return r;\n" +
            "}",*/
        //fak
        /*"int fak(int n)\n" +
            "{\n" +
            "  if (n == 0)\n" +
            "    return 1;\n" +
            "  return n * fak(n - 1);\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  return fak(6);\n" +
            "}",
        //prim-test
*/        "int prim(int n)\n" +
            "{\n" +
            "  int divisors, i;\n" +
            "  divisors = 0;\n" +
            "  i = 2;\n" +
            "  while (i < n)\n" +
            "  {\n" +
            "    if (n % i == 0)\n" +
            "      divisors = divisors + 1;\n" +
            "    i = i + 1;\n" +
            "  }\n" +
            "  if (divisors == 0 && n >= 2)\n" +
            "  {\n" +
            "    return 1;\n" +
            "  }\n" +
            "  else\n" +
            "  {\n" +
            "    return 0;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "  int prims;\n" +
            "  prims = 0;\n" +
            "  prims = prims + prim(997);\n" +
            "  prims = prims + prim(120);\n" +
            "  prims = prims + prim(887);\n" +
            "  prims = prims + prim(21);\n" +
            "  prims = prims + prim(379);\n" +
            "  prims = prims + prim(380);\n" +
            "  prims = prims + prim(757);\n" +
            "  prims = prims + prim(449);\n" +
            "  prims = prims + prim(5251);\n" +
            "  return prims;\n" +
            "}"
    };

    for(String code : codesWithBraceOnNextLine)
    {
      
      //ParsyMcParseface parser = new ParsyMcParseface(tokens);
      //Alternativ
      ParsyMcParseface parser = new ParsyMcParseface(code);
      Program program = parser.liefert();
      FormatVisitor fv = new FormatVisitor();
      program.accept(fv);
      //System.out.println(fv.getResult());
      assertEquals(code, fv.getResult());
    }
  }
}
