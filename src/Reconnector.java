import java.util.ArrayList;
import java.util.Random;

public class Reconnector {
    public static ArrayList<Agent> reconnct(ArrayList<Agent> agents, float beta) {
        for (Agent agent : agents) {
            agent.resetConnection();
        }

        connectAgents(agents);

        return pickUpWithBeta(agents, beta);
    }

    private static void connectAgents(ArrayList<Agent> agents) {
        for (Agent agent : agents) {
            for (Agent opponent : agents) {
                agent.connect(opponent);
            }
        }


    }

    private static ArrayList<Agent> pickUpWithBeta(ArrayList<Agent> agents, float beta) {
        int pickUpNum = (int) (agents.size() * beta);
        ArrayList<Agent> pickedUpAgents = new ArrayList<>();
        for (int i = 0; i < pickUpNum; i++) {
            while (true) {
                int random = new Random().nextInt(agents.size());
                if (!pickedUpAgents.contains(agents.get(random))) {
                    pickedUpAgents.add(agents.get(random));
                    break;
                }
            }
        }
        return pickedUpAgents;
    }

}
