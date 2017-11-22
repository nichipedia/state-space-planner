import edu.uno.ai.planning.ss.StateSpacePlanner;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import edu.uno.ai.planning.ss.StateSpaceSearch;

/**
 * Created by NicholasMoran on 11/21/17.
 */
public class NicksPlanner extends StateSpacePlanner {
    public NicksPlanner() { super("S-T-A-T-E Let me tell you what it means to me!"); }

    protected StateSpaceSearch makeStateSpaceSearch(StateSpaceProblem problem) {
        return new NicksStateSpaceSearch(problem, new FastForwardHeuristic(problem));
    }
}
