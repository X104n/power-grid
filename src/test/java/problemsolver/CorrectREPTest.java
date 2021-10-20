package problemsolver;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for Codegrade autograder.
 * @author Sondre Bolland
 *
 */
public class CorrectREPTest {

	private ProblemSolverREPTest tests = new ProblemSolverREPTest();
	
	@Test
	public void correctFunctionalityTest() throws IOException {
		tests.getProblemSolver();
		tests.test1();
		tests.test2();
		tests.test3();
		tests.test4();
		tests.test5();
	}
}