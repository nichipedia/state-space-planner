import edu.uno.ai.planning.Heuristic;
import edu.uno.ai.planning.Plan;
import edu.uno.ai.planning.TotalOrderPlan;
import edu.uno.ai.planning.pg.*;
import edu.uno.ai.planning.ss.StateSpaceNode;
import edu.uno.ai.planning.ss.StateSpaceProblem;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by NicholasMoran on 11/21/17.
 */
public class FastForwardHeuristic implements Heuristic {

    private PlanGraph graph;

    public FastForwardHeuristic(StateSpaceProblem problem) {
        graph = new PlanGraph(problem, false, false);
    }

    @Override
    public double evaluate(Object object) {
        StateSpaceNode stateNode = (StateSpaceNode)object;
        graph.initialize(stateNode.state);
        while(!graph.goalAchieved()) {
            System.out.println("YAS");
            graph.extend();
        }
        Subgraph subgraph = addGoals(graph);
        int level = graph.size() - 1;
        Plan plan = findPlan(subgraph, subgraph, level);
        return (double)plan.size();
    }

    private static final Plan findPlan(Subgraph subgraph, Subgraph lightGraph, int level) {
        while(level != 0) {
            if(subgraph.size() != 0 && !(subgraph.first.node instanceof StepNode)) {
                for(StepNode step:((LiteralNode) subgraph.first.node).getProducers(level)) {
                    if (!lightGraph.contains(step, level)) {
                        lightGraph = lightGraph.add(step, level);
                        for (LiteralNode precondition : step.getPreconditions(level)) {
                            lightGraph = lightGraph.add(precondition, level - 1);
                        }
                    }
                    return findPlan(subgraph.rest, lightGraph, level);
                }
            }
            --level;
            subgraph = lightGraph;
        }
        TotalOrderPlan plan = new TotalOrderPlan();
        for (NodeInstance light:lightGraph) {
            if (light.node instanceof StepNode) {
                plan = plan.addStep(((StepNode) light.node).step);
            }
        }
        return plan;
    }

    public final Subgraph addGoals(PlanGraph temp) {
        Subgraph subgraph = new Subgraph();
        for(LiteralNode goal:temp.goals) {
            subgraph = subgraph.add(goal, temp.size() - 1);
        }
        return subgraph;
    }
}
