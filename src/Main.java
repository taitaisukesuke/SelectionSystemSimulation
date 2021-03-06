import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.StringArrayListHandler;

import javax.print.attribute.IntegerSyntax;


public class Main {
    private AgentGroup[] agentGroups;
    private final Company company;
    private String outputpath = "output/";
    private EvaluateListener evaluateListener;
    private boolean isSelectionSystem;


    public static void main(String[] args) {

        for (int x = 0; x < Setting.TIMES_OF_SIMULATING; x++) {

            Main main = new Main(Setting.AGENT_GROUP_NUM, Setting.AGENT_NUM_IN_ONE_GROUP, Setting.BETA, Setting.GYOUMU_NUM, Setting.PERCENTAGE_OF_LEANING, Setting.SYUYAKUDO,Setting.IS_SELECTION_SYSTEM,null);

            runMain(main);
            System.out.println(x);
        }
    }

    public static void runMain(Main main){
        main.createNetwork();
        int times = 0;
        for (int i = 0; i < Setting.UPDATE_NUM; i++) {
            main.evaluating();
            main.learning();
            times++;


            if (times == Setting.CHANGE_CONNECTION_TIME) {
                main.reconnect();
                times=0;
            }
        }

    }


    public Main(int agentGroupNum, int agentNum, float beta, int gyoumuNum, int percentage, int syuyakudo,boolean isSelectionSystem, EvaluateListener forDemoListener) {
        this.isSelectionSystem=isSelectionSystem;
        Date d = new Date();
        SimpleDateFormat d1 = new SimpleDateFormat("MM_dd_HH_mm_ss.SSS");
        outputpath = outputpath + d1.format(d) + "," + Setting.BETA + "," + Setting.SYUYAKUDO + "," + Setting.IS_SELECTION_SYSTEM;


        agentGroups = new AgentGroup[agentGroupNum];

        for (int i = 0; i < agentGroups.length; i++) {
            agentGroups[i] = new AgentGroup(agentNum, beta, i, gyoumuNum, percentage);
        }

        company = new Company(gyoumuNum, syuyakudo);


        if(forDemoListener==null){
            List<String[]> first = new ArrayList<>();
            first.add(Arrays.stream(agentGroups)
                    .flatMap(agentGroup -> agentGroup.getAgents().stream().map(agent -> agent.getMyagentGroup().getId() + "--" + agent.getId()))
                    .toArray(String[]::new));
            try {
                Csv.save(first, new FileOutputStream(outputpath + ".csv", true), new CsvConfig(), new StringArrayListHandler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            evaluateListener = forDemoListener;
        }
    }

    public void createNetwork() {
        ArrayList<Agent> pickedUpAgents = new ArrayList<>();
        for (int i = 0; i < agentGroups.length; i++) {
            pickedUpAgents.addAll(agentGroups[i].pickUpAgentsWithBeta());
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

    public void learning() {
        for (AgentGroup agentGroup : agentGroups) {
            agentGroup.learningAllAgents();
        }
    }

    public void evaluating() {
        List<String[]> result = new ArrayList<>();
        result.add(Arrays.stream(agentGroups)
                .flatMap(agentGroup -> agentGroup.getAgents().stream().map(agent -> String.valueOf(company.evaluate(agent))))
                .toArray(String[]::new));

        if(evaluateListener!=null){
            OptionalDouble average = Arrays.stream(agentGroups)
                    .flatMapToInt(agentGroup -> agentGroup.getAgents().stream().mapToInt(company::evaluate)).average();

            if(average.isPresent()){
                System.out.println(average.getAsDouble());
                evaluateListener.onEvaluated(average.getAsDouble());
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
                    try {
            Csv.save(result, new FileOutputStream(outputpath + ".csv", true), new CsvConfig(), new StringArrayListHandler());

        } catch (IOException e) {
            e.printStackTrace();
        }
        }



    }

    public void reconnect() {
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
        if (Setting.IS_SELECTION_SYSTEM) {
            for (int i = 0; i < Setting.AGENT_GROUP_NUM * Setting.AGENT_NUM_IN_ONE_GROUP; i++) {
                selectedAgents.add(allAgents.get(i));
                times++;

                if (times == Setting.AGENT_NUM_IN_ONE_GROUP) {
                    agentsPickedUpWithBeta.addAll(Reconnector.reconnct(selectedAgents, Setting.BETA));
                    selectedAgents.clear();
                    times = 0;
                }
            }
        } else {
            for (int i = 0; i < Setting.AGENT_GROUP_NUM; i++) {
                for (int j = 0; j < Setting.AGENT_GROUP_NUM * Setting.AGENT_NUM_IN_ONE_GROUP; j++) {
                    if (j % Setting.AGENT_GROUP_NUM == i) {
                        selectedAgents.add(allAgents.get(j));
                    }
                }
                agentsPickedUpWithBeta.addAll(Reconnector.reconnct(selectedAgents, Setting.BETA));
                selectedAgents.clear();
            }
        }
        connectPickedUpAgents(agentsPickedUpWithBeta);
//        System.out.println("メンバーチェンジ！");
    }


    interface EvaluateListener{
        void onEvaluated(double average);
    }
}

