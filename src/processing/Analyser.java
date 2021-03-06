package processing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parser.tagBuilder.Constant;

import java.util.Map;

/**
 * This enumeration contains all the different analyses that we want
 * to do. The program will run the execute() method for all of them.
 */

public enum Analyser {
    RESOURCE_NEEDED {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList contracts = doc.getElementsByTagName(Constant.contract);
            Map<String, Integer> map = meter.getNeededResources();
            for (int i = 0; i < contracts.getLength(); i++) {
                Element contract = (Element) contracts.item(i);
                String res = contract.getElementsByTagName(Constant.resource).item(0).getTextContent();
                String qte = contract.getElementsByTagName(Constant.amount).item(0).getTextContent();
                map.put(res, Integer.parseInt(qte));
            }
        }
    },
    INITIAL_BUDGET {
        @Override
        public void execute(Document doc, DataMeter meter) {

            Node init = doc.getElementsByTagName(Constant.budget).item(0);
            meter.setInitialBudget(init.getTextContent());
        }
    },
    LENGTH_TRAVELED_AERIAL {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                String action = request.getElementsByTagName("action").item(0).getTextContent();
                if (action.equals("fly")) meter.lengthAerialPlus(3);
                if (action.equals("heading")) meter.lengthAerialPlus(6);
            }
        }
    },
    LENGTH_TRAVELED_TERRESTRIAL {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getElementsByTagName(Constant.request);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                String action = e.getElementsByTagName("action").item(0).getTextContent();
                if (action.equals("move_to")) meter.lengthTerrestrialPlus(1);
            }
        }
    },
    AERIAL_COST {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            boolean land = false;
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                Element answer = (Element) nodes.item(i + 2);
                String action = request.getElementsByTagName("action").item(0).getTextContent();
                String cost = answer.getElementsByTagName("cost").item(0).getTextContent();

                if (action.equals("land")) {
                    land = true;
                }
                if (!land) {
                    meter.aerialCostPlus(Integer.parseInt(cost));
                }
            }
        }
    }, TERRESTRIAL_COST {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            boolean land = false;
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                Element answer = (Element) nodes.item(i + 2);
                String action = request.getElementsByTagName("action").item(0).getTextContent();
                String cost = answer.getElementsByTagName("cost").item(0).getTextContent();

                if (action.equals("land")) {
                    land = true;
                }
                if (land) {
                    meter.terrestrialCostPlus(Integer.parseInt(cost));

                }
            }
        }
    },
    REMAINING_BUDGET {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getElementsByTagName(Constant.answer);
            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);
                String action = e.getElementsByTagName("cost").item(0).getTextContent();
                meter.budgetMinus(Integer.parseInt(action));
            }
        }
    },
    COLLECTED_RESOURCES {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            //gathered resources
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                Element answer = (Element) nodes.item(i + 2);
                if (request.getElementsByTagName("action").item(0).getTextContent().equals("exploit")) {
                    String res = request.getElementsByTagName("resource").item(0).getTextContent();
                    int qte = Integer.parseInt(answer.getElementsByTagName("amount").item(0).getTextContent());
                    meter.gatherResource(res, qte);
                }
            }

            // transformed resources
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                Element answer = (Element) nodes.item(i + 2);
                if (request.getElementsByTagName("action").item(0).getTextContent().equals("transform")) {
                    NodeList list = request.getElementsByTagName("unit");
                    for (int j = 0; j < list.getLength(); j++) {
                        Element e = (Element) list.item(j);
                        String res = e.getElementsByTagName("resource").item(0).getTextContent();
                        int qte = Integer.parseInt(e.getElementsByTagName("quantity").item(0).getTextContent());
                        meter.removeResource(res, qte);
                    }

                    String newRes = answer.getElementsByTagName("kind").item(0).getTextContent();
                    int newQte = Integer.parseInt(answer.getElementsByTagName("production").item(0).getTextContent());
                    meter.gatherResource(newRes, newQte);

                }
            }
        }

    },
    COST_PER_ACTION {
        @Override
        public void execute(Document doc, DataMeter meter) {
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 3; i < nodes.getLength(); i += 4) {
                Element request = (Element) nodes.item(i);
                Element answer = (Element) nodes.item(i + 2);

                String res = request.getElementsByTagName("action").item(0).getTextContent();
                String qte = answer.getElementsByTagName("cost").item(0).getTextContent();
                meter.actionPlus(res,Integer.parseInt(qte));

            }
        }
    };

    Analyser() {
    }

    public abstract void execute(Document doc, DataMeter meter);


}
