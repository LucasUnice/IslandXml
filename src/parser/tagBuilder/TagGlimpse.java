package parser.tagBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import parser.Tag;

public class TagGlimpse extends TagBuilderDirected {
    public TagGlimpse(JSONObject request, JSONObject answer,int id) {
        super(request, answer, Tag.GLIMPSE.getName(),id);
    }

    @Override
    public Element getActionXml(Document doc) {
        Element element = super.getActionXml(doc);

        NodeList list = element.getElementsByTagName(Constant.data);
        Element data =(Element) list.item(0);

        NodeList dataList = data.getElementsByTagName(Constant.parameters);
        Element parameters = (Element) dataList.item(0);

        Element range = doc.createElement(Constant.range);
        range.appendChild(doc.createTextNode(String.valueOf(request.getJSONObject(Constant.data).
                getJSONObject(Constant.parameters).
                getInt(Constant.range))));
        parameters.appendChild(range);

        return  element;
    }
    /*
    <request>
		<data>
			<cost>3</cost>
			<extras>
				<report>
					<tile>
						<resource>BEACH</resource>
						<percentage>59.35</percentage>
					</tile>
					<tile>
						<resource>OCEAN</resource>
					</tile>
				</report>
			</extras>
			<status>OK</status>
		</data>
		<part>Engine</part>
		<time>16815132</time>
	</request>
     */

    @Override
    public Element getAnswerXml(Document doc) {
        Element element = super.getAnswerXml(doc);

        NodeList list = element.getElementsByTagName(Constant.data);
        Element data =(Element) list.item(0);

        Element extras = (Element) data.getElementsByTagName(Constant.extras).item(0);

        Element report = doc.createElement(Constant.report);

        JSONArray arr = answer.getJSONObject(Constant.data)
                .getJSONObject(Constant.extras)
                .getJSONArray(Constant.report);

        buildTile(arr,report,doc);

        extras.appendChild(report);

        return element;
    }
    /*
    {
  "data": {
    "cost": 6,
    "extras": {
      "asked_range": 4,
      "report": [
        [[
          "SHRUBLAND",
          100
        ]],
        [[
          "SHRUBLAND",
          100
        ]],
        ["SHRUBLAND"],
        ["SHRUBLAND"]
      ]
    },
    "status": "OK"
  },
  "part": "Engine",
  "time": 1490548600302,
  "meth": "takeDecision"
}
     */

    private void buildTile(JSONArray array,Element extras,Document doc){

        for(int i = 0;i<array.length();i++){
            String tileString = "";
            String percString = "";


            try{
                tileString = array.getJSONArray(i).getJSONArray(0).getString(0);
                percString = String.valueOf(array.getJSONArray(i).getJSONArray(0).getInt(1));
            } catch (Exception e){
                tileString = array.getJSONArray(i).getString(0);
            }

            Element tile = doc.createElement(Constant.tile);

            Element resource = doc.createElement(Constant.resource);
            resource.appendChild(doc.createTextNode(tileString));
            tile.appendChild(resource);

            if(percString != ""){
                Element percentage = doc.createElement(Constant.percentage);
                percentage.appendChild(doc.createTextNode(percString));
                tile.appendChild(percentage);
            }

            extras.appendChild(tile);
        }



    }
}
