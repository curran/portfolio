package curranPhysics;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import variableBridge.IndividualBooleanVariableUpdater;
import variableBridge.IndividualDecimalVariableUpdater;
import variableBridge.IndividualIntegerVariableUpdater;
import variableBridge.IndividualVariableBridge;
import variableBridge.VariableBridge;
import variableEditorUI.VariableEditorUICommand;
import expressionConsole.ExpressionConsoleModel;

public class VariableBridgedCurranPhysics extends CurranPhysics_MovableBalls {
	private static final long serialVersionUID = 2908934904223362251L;

	public VariableBridgedCurranPhysics() {
		IndividualVariableBridge[] bridges = {
				new IndividualVariableBridge("Number_of_Balls", "",
						new IndividualIntegerVariableUpdater() {

							public int getInternalVariableValue() {
								return numBalls;
							}

							public void updateInternalVariable(int newValue) {
								numBalls = newValue;
							}
						}),
				new IndividualVariableBridge("Viscosity", "",
						new IndividualDecimalVariableUpdater() {

							public double getInternalVariableValue() {
								return viscosity;
							}

							public void updateInternalVariable(double newValue) {
								viscosity = newValue;
							}
						}),
				new IndividualVariableBridge("Attractive_Force", "",
						new IndividualDecimalVariableUpdater() {

							public double getInternalVariableValue() {
								return attractiveForceStrength;
							}

							public void updateInternalVariable(double newValue) {
								attractiveForceStrength = newValue;
							}
						}),

				new IndividualVariableBridge("Repulsive_Force", "",
						new IndividualDecimalVariableUpdater() {

							public double getInternalVariableValue() {
								return repulsiveForceStrength;
							}

							public void updateInternalVariable(double newValue) {
								repulsiveForceStrength = newValue;
							}
						}),
				new IndividualVariableBridge("Coulombic_Force", "",
						new IndividualDecimalVariableUpdater() {

							public double getInternalVariableValue() {
								return coulombicForceStrength;
							}

							public void updateInternalVariable(double newValue) {
								coulombicForceStrength = newValue;
								setCharges();
							}
						}),
						new IndividualVariableBridge("Gravity", "",
								new IndividualDecimalVariableUpdater() {

									public double getInternalVariableValue() {
										return gravity;
									}

									public void updateInternalVariable(double newValue) {
										gravity = newValue;
									}
								}),new IndividualVariableBridge("In_3D", "",
										new IndividualBooleanVariableUpdater() {

									public boolean getInternalVariableValue() {
										return in3D;
									}

									public void updateInternalVariable(boolean newValue) {
										in3D = newValue;
									}
								})
				
		
				};

		// put the variable bridges into action
		VariableBridge variableBridge = new VariableBridge(bridges);
		variableBridge.showEditorForAllVariables();
	}

	

	public static void main(String[] args) {
		// set the native system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// enable the edit() command
		VariableEditorUICommand
				.setUpEditVariablesCommand(ExpressionConsoleModel.getInstance()
						.getParser());

		JFrame f = new JFrame("CurranPhysics");
		f.getContentPane().add(new VariableBridgedCurranPhysics());
		f.setBounds(500, 200, 400, 400);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
