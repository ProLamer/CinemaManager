package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class AllButtons {
	public static String nameZal= "���� ���";
	public static List<Node> allButtons = new ArrayList<>();
	public static void syso(){
		System.out.println(allButtons);
	}
	public static void setTooltips(Button refresh,Button showOnlinePaidButton,Button bookButton,Button rebookButton,Button paidButton){
		Tooltip tooltipForOnlinePaid = new Tooltip();
		Tooltip tooltipForRefrsh = new Tooltip();
		Tooltip tooltipForBookButton = new Tooltip();
		Tooltip tooltipForRebookButton = new Tooltip();
		Tooltip tooltipForPaidButton = new Tooltip();
		
		tooltipForRefrsh.setText(
				"��������"
				);
		tooltipForOnlinePaid.setText(
		    "�������� ������� ������ ����" 
				);
		tooltipForBookButton.setText(
				"�����������"
				);
		tooltipForRebookButton.setText(
				"����� ����������"
				);
		tooltipForPaidButton.setText(
				"��������"
				);
		
		refresh.setTooltip(tooltipForRefrsh);
		showOnlinePaidButton.setTooltip(tooltipForOnlinePaid);
		bookButton.setTooltip(tooltipForBookButton);
		rebookButton.setTooltip(tooltipForRebookButton);
		paidButton.setTooltip(tooltipForPaidButton);
	}
}
