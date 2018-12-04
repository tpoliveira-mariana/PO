package sth;

import java.util.List;
import sth.exceptions.NoSuchDisciplineSelectionException;

interface Observer {

	public void receiveNotification(String notification);
	public List<String> seeAllNotifications();
	public void disableNotifications(String discName) throws NoSuchDisciplineSelectionException;
	public void enableNotifications(String discName) throws NoSuchDisciplineSelectionException;
	public int getId();
}