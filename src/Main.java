import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.StringArrayListHandler;


public class Main {
    private AgentGroup[] agentGroups;
    private final Company company;
    private String outputpath = "output/";

    public static void main(String[] args) {
        Main main = new Main(Setting.AGENT_GROUP_NUM, Setting.AGENT_NUM_IN_ONE_GROUP, Setting.BETA, Setting.GYOUMU_NUM, Setting.PERCENTAGE_OF_LEANING, Setting.SYUYAKUDO);
        main.createNetwork();

        int times = 0;
        for (int i = 0; i < Setting.UPDATE_NUM; i++) {
            main.evaluating();
            main.learning();
            times++;

            if (Setting.IS_SELECTION_SYSTEM) {
                if (times == Setting.CHANGE_CONNECTION_TIME) {
                    main.reconnect();
                }
            }
        }


    }

    private Main(int agentGroupNum, int agentNum, float beta, int gyoumuNum, int percentage, int syuyakudo) {
        Date d = new Date();
        SimpleDateFormat d1 = new SimpleDateFormat("MM_dd_HH_mm_ss");
        outputpath += d1.format(d);


        agentGroups = new AgentGroup[agentGroupNum];

        for (int i = 0; i < agentGroups.length; i++) {
            agentGroups[i] = new AgentGroup(agentNum, beta, i, gyoumuNum, percentage);
        }

        company = new Company(gyoumuNum, syuyakudo);

        List<String[]> first = new ArrayList<>();


        first.add(Arrays.stream(agentGroups)
                .flatMap(agentGroup -> agentGroup.getAgents().stream().map(agent -> agent.getMyagentGroup().getId() + "--" + agent.getId()))
                .toArray(String[]::new));

        try {
            Csv.save(first, new FileOutputStream(outputpath + ".csv", true), new CsvConfig(), new StringArrayListHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void createNetwork() {
        ArrayList<Agent> pickedUpAgents = new ArrayList<>();
        for (int i = 0; i < agentGroups.length; i++) {
            pickedUpAgents.addAll(agentGroups[i].pickUpAgentssWithBeta());
        }
        connectPickedUpAgents(pickedUpAgents);
    }

    private void connectPickedUpAgents(ArrayList<Agent> agents) {
        Random random = new Random();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);

            while (true) {
                int randNum = random.nextInt(agents.size());
                Agent randomAgent = agents.get(randNum);

                if (randNum != i && !randomAgent.isConnected(agent)) {
                    agent.connect(randomAgent);

                    break;
                }
            }
        }
    }

    private void learning() {
        for (AgentGroup agentGroup : agentGroups) {
            agentGroup.learningAllAgents();
        }
    }

    private void evaluating() {
        List<String[]> result = new ArrayList<>();
        result.add(Arrays.stream(agentGroups)
                .flatMap(agentGroup -> agentGroup.getAgents().stream().map(agent -> String.valueOf(company.evaluate(agent))))
                .toArray(String[]::new));

        try {
            Csv.save(result, new FileOutputStream(outputpath + ".csv", true), new CsvConfig(), new StringArrayListHandler());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconnect() {
        ArrayList<Agent> allAgents = new ArrayList<>();

        for (AgentGroup agentGroup : agentGroups) {
            for (Agent agent : agentGroup.getAgents()) {
                allAgents.add(agent);
            }

            Collections.sort(allAgents, new AgentComparator());
        }

        ArrayList<Agent> selectedAgents = new ArrayList<>();
        ArrayList<Agent> agentsPickedUpWithBeta = new ArrayList<>();
        int times = 0;

        for (int i = 0; i < Setting.AGENT_GROUP_NUM * Setting.AGENT_NUM_IN_ONE_GROUP; i++) {
            selectedAgents.add(allAgents.get(i));
            times++;

            if (times == Setting.AGENT_NUM_IN_ONE_GROUP) {
                agentsPickedUpWithBeta.addAll(Reconnector.reconnct(selectedAgents, Setting.BETA));
                selectedAgents.clear();
                times = 0;
            }
        }

        connectPickedUpAgents(agentsPickedUpWithBeta);
        System.out.println("メンバーチェンジ！");
    }
}

