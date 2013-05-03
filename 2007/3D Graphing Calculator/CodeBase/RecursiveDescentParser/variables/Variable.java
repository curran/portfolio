package variables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import parser.ExpressionNode;
import parser.Value;
import valueTypes.DecimalValue;

/**
 * The class which represents a variable, and also statically manages all
 * existing variables. All variables are global.
 * 
 * When Variables are compa
 * 
 * @author Curran
 * 
 */
public class Variable extends ExpressionNode {
	/**
	 * The name of the variable.
	 */
	String name;

	/**
	 * The value contained in the variable.
	 */
	Value value;

	/**
	 * A flag denoting that this Variable has Observers
	 */
	boolean hasObservers = false;

	/**
	 * If set to false, then calling set() in this variable will do nothing.
	 */
	public boolean modifiable = true;

	/**
	 * The interal observable which acts on behalf of the ObservableVariable
	 * class, since it cannot extend Observable itself because it is already
	 * extending Variable.
	 */
	ObservableVariableInternalObservable internalObservable = null;

	/**
	 * The symbol table all global variables. the type is <String name,Variable
	 * variable>
	 */
	static Map<String, Variable> variables = new HashMap<String, Variable>();

	/**
	 * The explainations for why each observer was added.
	 */
	Map<Observer, String> observerExplainations = new HashMap<Observer, String>();

	/**
	 * the most rescent system time at which the set() method was called on this
	 * Variable.
	 */
	private long timeOfLastChange = 0;

	/**
	 * Constrict aVariable with the specified name, and a default value of 0.
	 * 
	 * @param name
	 *            the variable name
	 */
	protected Variable(String name) {
		this.name = name;
		value = new DecimalValue(0);
	}

	/**
	 * Gets a reference to an existing variable with the specified name. If a
	 * variable exists with that name, then a new one is created, mapped to it's
	 * name, and returned.
	 * 
	 * @param variableName
	 *            the name of the vaiable
	 * @return the variable with the name variableName
	 */
	public static Variable getVariable(String variableName) {
		Variable variable = variables.get(variableName);
		if (variable != null)
			return variable;

		// if we are here then we are creating new variable
		Variable newVariable = new Variable(variableName);
		variables.put(variableName, newVariable);
		return newVariable;
	}

	/**
	 * Returns the value of this variable, with no side effect whatsoever.
	 * Inherited from ExpressionNode.
	 */
	public Value evaluate() {
		return value;
	}

	/**
	 * @return the name of this Variable.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Gets the symbol table which maps variable names to Variable objects.
	 * 
	 * @return the symbol table
	 */
	public static Map<String, Variable> getSymbolTable() {
		return variables;
	}

	/**
	 * Adds an Observer to this variable which is notified whenever the variable
	 * changes. The argument Object in the notification is a reference to the
	 * Variable which changed.
	 * 
	 * @param o
	 *            the Observer to add to the Variable
	 * @param explaination
	 *            a human readable explaination of why this observer is being
	 *            added. Should be phrased as an aside to the Variable name, for
	 *            example, "Observed by the text box so the text is updated when
	 *            it changes.
	 */
	public void addObserver(Observer o, String explaination) {
		observerExplainations.put(o, explaination);
		if (internalObservable == null)
			internalObservable = new ObservableVariableInternalObservable();
		internalObservable.addObserver(o);
		hasObservers = true;
	}

	/**
	 * Deletes an observer from the set of observers of this object.
	 * 
	 * @param o
	 *            the observer to be deleted.
	 */
	public void deleteObserver(Observer o) {
		internalObservable.deleteObserver(o);
		observerExplainations.remove(o);
	}

	/**
	 * Sets the value of this Variable
	 * 
	 * @param value
	 *            the value to set this variable to.
	 * @return the value this variable is set to.
	 */
	public Value set(Value value) {
		if (modifiable) {
			// assign the value
			this.value = value;

			// update the observers if there are any
			if (hasObservers)
				internalObservable.setChangedAndNotifyObservers(this);

			// update the time of last change
			timeOfLastChange = System.currentTimeMillis();
		}
		return this.value;
	}

	/**
	 * The explainations for why each observer was added.
	 * 
	 * @return a collection of strings, one explaining each Observer.
	 */
	public Collection<String> getObserverExplainations() {
		return observerExplainations.values();
	}

	/**
	 * the ObservableVariable class cannot extend Observable because it is
	 * already extending Variable, so this inner class must be created to be the
	 * actual Observable.
	 */
	private class ObservableVariableInternalObservable extends Observable {

		/**
		 * Sends a notification to all Observers that the variable has changed.
		 * 
		 * @param variable
		 *            the ObservableVariable which will be passed as the
		 *            notification argument.
		 * 
		 */
		public void setChangedAndNotifyObservers(Variable variableWhichChanged) {
			setChanged();
			notifyObservers(variableWhichChanged);
		}
	}

	/**
	 * 
	 * @return true if this Variable is being observed, false if not.
	 */
	public boolean hasObservers() {
		return hasObservers;
	}

	/**
	 * Returns the most rescent system time at which the set() method was called
	 * on this Variable.
	 */
	public long getTimeOfLastChange() {
		return timeOfLastChange;
	}
}
