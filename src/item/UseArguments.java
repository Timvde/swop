package item;

import java.util.ArrayList;
import java.util.List;

/**
 * Use arguments adds additional arguments to the use method of an item. This
 * class should not be created by the user, but retrieved from the item by the
 * {@link IItem#getUseArguments()} method. This allows the item to give a list
 * of possible choices to the client.
 * 
 * @param <E>
 *        The type of elements the user can chose
 */
public class UseArguments<E> {
	
	private List<E>	choices;
	private String	question;
	private E		userChoice;
	
	/**
	 * Create a new use arguments class with a specified set of choices and a
	 * question to display to the user.
	 * 
	 * @param choices
	 *        the choices for the user
	 * @param question
	 *        A question that gives extra info about what response is expected
	 *        from the client
	 */
	public UseArguments(List<E> choices, String question) {
		this.choices = choices;
		this.question = question;
	}
	
	/**
	 * A question with extra information about why and what response is expected
	 * from the user.
	 * 
	 * @return question why the choice is needed by the client
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * Returns a set of the possible answers. If the returned set is empty, any
	 * object of type E can be set as the return value by the client.
	 * 
	 * @return set of the possible answers
	 */
	public List<E> getPossibleAnswers() {
		return new ArrayList<E>(choices);
	}
	
	/**
	 * Returns the choice of the client. If the client has not (yet) set a
	 * choice, null will be returned.
	 * 
	 * @return the choice of the user
	 */
	public E getUserChoise() {
		return userChoice;
	}
	
	/**
	 * Set the choice of the user. The index is retrieved from the order defined
	 * by {@link #getPossibleAnswers()}.
	 * 
	 * @param index
	 *        The index of the choice the user made from
	 *        {@link #getPossibleAnswers()}
	 */
	public void setUserChoice(int index) {
		this.userChoice = getPossibleAnswers().get(index);
	}
}
