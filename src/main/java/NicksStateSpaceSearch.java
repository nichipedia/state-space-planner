import edu.uno.ai.planning.Heuristic;
import edu.uno.ai.planning.Plan;
import edu.uno.ai.planning.Step;
import edu.uno.ai.planning.ss.StateSpaceNode;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import edu.uno.ai.planning.ss.StateSpaceSearch;
import edu.uno.ai.planning.util.MinPriorityQueue;

/**
 * Created by NicholasMoran on 11/21/17.
 */
public class NicksStateSpaceSearch extends StateSpaceSearch {
    private Heuristic heuristic;
    private final MinPriorityQueue frontier = new MinPriorityQueue();

    public NicksStateSpaceSearch(StateSpaceProblem problem, Heuristic heuristic) {
        super(problem);
        this.heuristic = heuristic;
        this.frontier.push(this.root, heuristic.evaluate(this.root));
    }

    @Override
    public final Plan findNextSolution() {
        while(!this.frontier.isEmpty()) {
            StateSpaceNode stateNode = (StateSpaceNode)this.frontier.pop();
            for(Step step:problem.steps) {
                if(step.precondition.isTrue(stateNode.state)) {
                    StateSpaceNode newState = stateNode.expand(step);
                    double eval = this.heuristic.evaluate(newState);
                    //System.out.println(eval);
                    if (eval != Double.POSITIVE_INFINITY) {
                        this.frontier.push(newState, newState.plan.size() + eval);
                    }
                }
            }

            if(this.problem.goal.isTrue(stateNode.state)) {
                return stateNode.plan;
            }
        }
        return null;
    }
}
