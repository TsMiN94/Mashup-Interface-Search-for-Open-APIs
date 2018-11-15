package miso.component.pref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import miso.actions.keyboard.TransFormat;
import miso.resource.InputVirtualKeys;
import miso.utility.PrefInfo;
import miso.utility.Scope;

public class ApplyandCloseButton extends JFXButton {

	private FileOutputStream out = null;
	private static final String prefInfo = "./preference/prefInfo.txt";
	private Scope scope = Scope.getInstace();

	public ApplyandCloseButton() {
		addEventHandler(ActionEvent.ACTION, e -> {
			applyandClose(e);
			scope.setPrefViewFlag(false);
		});
	}

	private void applyandClose(ActionEvent e) {
		System.out.println("apply and close!");
		PrefInfo pref = scope.getPrefInfo();
		String[] line = new String[9];

		for (int in = 0; in < Scope.getInstace().getPrefBorder().size(); in++) {
			BorderPane setting = Scope.getInstace().getPrefBorder().get(in);
			AnchorPane center = (AnchorPane) setting.getCenter();
			switch (setting.getId()) {
			case "dicBorder":
				Pane dicPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < dicPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) dicPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setDicAPI(selected.getText());
						line[0] = "dic:" + selected.getText() + "\n";
						break;
					}
				}
				break;
			case "encBorder":
				Pane encPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < encPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) encPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setEncAPI(selected.getText());
						line[1] = "enc:" + selected.getText() + "\n";
						break;
					}
				}
				break;
			case "imgBorder":
				Pane imgPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < imgPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) imgPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setImgAPI(selected.getText());
						line[2] = "img:" + selected.getText() + "\n";
						break;
					}
				}
				break;
			case "trsBorder":
				Pane tlsPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < tlsPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) tlsPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setTrsAPI(selected.getText());
						line[3] = "trs:" + selected.getText() + "\n";
						break;
					}
				}
				break;
			case "mapBorder":
				Pane mapPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < mapPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) mapPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setMapAPI(selected.getText());
						line[4] = "map:" + selected.getText() + "\n";
						break;
					}
				}
				break;
			case "videoBorder":
				Pane videoPane = (Pane) center.getChildren().get(0);
				for (int i = 0; i < videoPane.getChildren().size(); i++) {
					JFXRadioButton selected = (JFXRadioButton) videoPane.getChildren().get(i);
					if (selected.isSelected()) {
						pref.setVideoAPI(selected.getText());
						line[5] = "video:" + selected.getText() + "\n";
						break;
					}
				}
				break;

			case "hotBorder":
				Pane hotPane = (Pane) center.getChildren().get(0);
				Text hotText = (Text) hotPane.getChildren().get(0);
				String hotData = hotText.getText().trim();
				
				Pane ocrPane = (Pane) center.getChildren().get(2);
				Text ocrText = (Text) ocrPane.getChildren().get(0);
				String ocrData = ocrText.getText().trim();
				
	//			System.out.println(hotData+"hot data!");
	//			 hotcombination = null;
				if(hotData.length() > 2 && !hotData.equals("mouse middle")) {
					String lineData="";
					String []hotcombination = hotData.split("\\+");
					lineData = "hot:";
					for(int i=0;i<hotcombination.length;i++) {
						if(hotcombination[i].equals(ocrText)) {
							Platform.runLater(new Runnable() {	
								@Override
								public void run() {
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("WARNING");
									alert.setHeaderText(null);
									alert.setContentText("Hot Key�� Ocr Key�� ��Ĩ�ϴ�. �ٽ� �������ּ���");
									alert.setResizable(false);
									alert.show();
								}
							});
							Platform.setImplicitExit(false);
							return;
						}	
						lineData += TransFormat.getInstance().getVirtualCode(hotcombination[i]);
						if(i<hotcombination.length-1)
							lineData += ",";
					}
					line[6] = lineData;
				}else {
					if(hotData.equals(ocrData)) {
						Platform.runLater(new Runnable() {	
							@Override
							public void run() {
								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("WARNING");
								alert.setHeaderText(null);
								alert.setContentText("Hot Key�� Ocr Key�� ��Ĩ�ϴ�. �ٽ� �������ּ���");
								alert.setResizable(false);
								alert.show();
							}
						});
						Platform.setImplicitExit(false);
						return;
					}	
					line[6] ="hot:" + TransFormat.getInstance().getVirtualCode(hotData);
				}
				line[6] += "\n";
				line[7] = "ocr:" + TransFormat.getInstance().getVirtualCode(ocrData)+"\n";
				break;
			}
		}
		String doc = "";
		for (int i = 0; i < 8; i++) {
			doc += line[i];
		}

		try {
			out = new FileOutputStream(prefInfo);
			byte bt[] = new byte[1024];
			bt = doc.getBytes();
			out.write(bt);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Scope.getInstace().setPrefInfo(pref);
		Scope.getInstace().getPrefStage().hide();
	}
}