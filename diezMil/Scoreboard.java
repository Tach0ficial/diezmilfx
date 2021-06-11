package diezMil;

/**
 * Class Scoreboard Guarda un HTML con el marcador final
 * 
 * @author Carlos Hidalgo Risco y Laura Hidalgo Rivera
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import diezMil.game.Player;

public class Scoreboard {

  String fileName;
  ArrayList<Player> players;
  int numPlayers;
  int rounds;

  Scoreboard(String fileName, int numPlayers, ArrayList<Player> players, int rounds) {
    this.fileName = fileName;
    this.numPlayers = numPlayers;
    this.players = players;
    this.rounds = rounds;
  }

  void saveHTML() {
    try {
      Document html = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      Element root = saveRoot(html);
      Element body = saveBody(html, root);

      saveStyle(html, root);
      saveTable(html, body);

      // Guardar HTML en fichero
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(html);
      StreamResult result = new StreamResult(new FileWriter(fileName));
      transformer.transform(source, result);
      System.out.println("El marcador se guardó correctamente.");
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    }

  }

  private Element saveRoot(Document html) {
    Element root = html.createElement("html");
    html.appendChild(root);

    Attr lang = html.createAttribute("lang");
    lang.setValue("es");
    root.setAttributeNode(lang);

    Element head = html.createElement("head");
    root.appendChild(head);

    Element meta = html.createElement("meta");
    head.appendChild(meta);

    Attr charset = html.createAttribute("charset");
    charset.setValue("UTF-8");
    head.setAttributeNode(charset);

    Element title = html.createElement("title");
    title.appendChild(html.createTextNode("Marcador 10000"));
    head.appendChild(title);
    return root;
  }

  private void saveStyle(Document html, Element root) {
    Element style = html.createElement("style");
    style.appendChild(html.createTextNode("h1{" + "    text-align: center;" + "  }" + "  table {"
        + "  width: 100%;" + "  text-align: center;" + "  border-collapse: collapse;" + "  }"
        + "  table td:nth-child(even) {" + "    background: #EBEBEB;" + "    padding: 3px 4px;"
        + "  }" + "  table thead {" + "    background: #FFFFFF;" + "    border: 1px solid #333333;"
        + "  }" + "  table th, td{" + "    text-align: center;"
        + "    border-left: 1px solid #333333;" + "  }" + "  table tbody, table tfoot {"
        + "    border: 1px solid #333333" + "  }"));
    root.appendChild(style);
  }

  private Element saveBody(Document html, Element root) {
    Element body = html.createElement("body");
    root.appendChild(body);

    Element h1 = html.createElement("h1");
    h1.appendChild(html.createTextNode("Marcador 10000"));
    body.appendChild(h1);
    return body;
  }

  private void saveTable(Document html, Element body) {
    Element table = html.createElement("table");
    body.appendChild(table);

    saveThead(html, table);
    saveTbody(html, table);
    saveTfoot(html, table);
  }

  private void saveThead(Document html, Element table) {
    Element thead = html.createElement("thead");
    table.appendChild(thead);

    Element tr = html.createElement("tr");
    thead.appendChild(tr);

    Element th = html.createElement("th");
    tr.appendChild(th);

    for (int i = 0; i < this.numPlayers; i++) {
      Element thPlayerName = html.createElement("th");
      thPlayerName.appendChild(html.createTextNode(this.players.get(i).getName()));
      tr.appendChild(thPlayerName);
    }
  }

  private void saveTbody(Document html, Element table) {
    Element tbody = html.createElement("tbody");
    table.appendChild(tbody);

    for (int i = 0; i < this.rounds; i++) {
      Element trRound = html.createElement("tr");
      tbody.appendChild(trRound);
      Element thRound = html.createElement("th");
      thRound.appendChild(html.createTextNode("Ronda " + (i + 1)));
      trRound.appendChild(thRound);
      for (int j = 0; j < this.numPlayers; j++) {
        if (i < this.players.get(j).getRoundPoints().size()) {
          Element tdRound = html.createElement("td");
          tdRound.appendChild(
              html.createTextNode(String.valueOf(this.players.get(j).getRoundPoints().get(i))));
          trRound.appendChild(tdRound);
        } else {
          Element tdRonda = html.createElement("td");
          trRound.appendChild(tdRonda);
        }
      }
    }
  }

  private void saveTfoot(Document html, Element table) {
    Element tfoot = html.createElement("tfoot");
    table.appendChild(tfoot);

    Element trTotalPoints = html.createElement("tr");
    tfoot.appendChild(trTotalPoints);

    Element thTotalPoints = html.createElement("th");
    trTotalPoints.appendChild(thTotalPoints);

    for (int i = 0; i < this.numPlayers; i++) {
      Element thTotalPoint = html.createElement("th");
      thTotalPoint
          .appendChild(html.createTextNode(String.valueOf(this.players.get(i).getTotalPoints())));
      thTotalPoints.appendChild(thTotalPoint);
    }
  }
}
