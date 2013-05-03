package grapher3D.controller;

import expressionConsole.CommandExecutingMenuItem;
import expressionConsole.ExpressionConsoleModel;
import grapher3D.Grapher3DConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;

import logEnabledComponents.LogEnabledJRadioButtonMenuItem;
import parser.RecursiveDescentParser;
import valueTypes.StringValue;
import variables.Variable;

/**
 * A panel which contains radio buttons for selecting the coordinate space to
 * use.
 * 
 * @author Curran Kelleher
 * 
 */
public class CoordinateSystemSelectorMenu extends JMenu {
	private static final long serialVersionUID = -893328529774054453L;

	/**
	 * the CoordinateSystemTranslator which the radio buttons will control.
	 */
	CoordinateSystemTranslator translator;

	/**
	 * The current CoordinateSystem being used. Whenever a radio button is
	 * clicked, the state of this CoordinateSystem is saved, then it is swapped
	 * out for the new one.
	 */
	CoordinateSystem currentCoordinateSystem = null;

	/**
	 * Creates a CoordinateSystemSelectorPanel bound to the specified
	 * CoordinateSystemTranslator.
	 * 
	 * @param translator
	 *            the CoordinateSystemTranslator which the radio buttons will
	 *            control.
	 */
	public CoordinateSystemSelectorMenu(CoordinateSystemTranslator translator) {
		super("Coordinate System");
		setMnemonic('C');

		this.translator = translator;

		// create the buttons and add them to the panel
		ButtonGroup radioButtons = new ButtonGroup();
		for (Iterator<CoordinateSystem> i = createCoordinateSystems()
				.iterator(); i.hasNext();) {
			CoordinateSystem currentCoordinateSystem = i.next();
			CoordinateSystemButton currentButton = new CoordinateSystemButton(
					currentCoordinateSystem);
			radioButtons.add(currentButton);
			add(currentButton);
			add(new CommandExecutingMenuItem("Edit Range", 'e',
					currentCoordinateSystem.editRangeCommand));
			addSeparator();
		}

		// select the first one initially
		radioButtons.getElements().nextElement().doClick(0);
	}

	/**
	 * Creates the CoordinateSystems.
	 */
	private List<CoordinateSystem> createCoordinateSystems() {
		RecursiveDescentParser parser = ExpressionConsoleModel.getInstance()
				.getParser();
		List<CoordinateSystem> coordinateSystems = new LinkedList<CoordinateSystem>();
		coordinateSystems
				.add(new CoordinateSystem(
						"Cartesian  z = f(x,y)",
						'c',
						"executeFunction({x=u*(xMax-xMin)+xMin;y=v*(yMax-yMin)+yMin;#;x=(x-xMin)/(xMax-xMin)*20-10;y=(y-yMin)/(yMax-yMin)*20-10})",
						"z = sin((x^2+y^2)*((sin(t)+1)/20)+t/2)",
						"CartesianCoordinateSystem",
						"edit(xMax,xMin,yMax,yMin)"));
		parser.parse("executeFunction({xMin=-10;xMax=10;yMin=-10;yMax=10})")
				.evaluate();

		coordinateSystems
				.add(new CoordinateSystem(
						"Cylindrical  z = f(r,theta)",
						'y',
						"executeFunction({r=u*(rMax-rMin)+rMin;theta=v*(thetaMax-thetaMin)+thetaMin;#;x=r*cos(theta);y=r*sin(theta)})",
						"z = -(r^2/10)+5+sin(r^2/10-t+theta)",
						"CylindricalCoordinateSystem_r_dependent",
						"edit(rMax,rMin,thetaMax,thetaMin)"));
		parser.parse(
				"executeFunction({rMin=0;rMax=10;thetaMin=0;thetaMax=2*pi})")
				.evaluate();

		coordinateSystems
				.add(new CoordinateSystem(
						"Cylindrical  r = f(z,theta)",
						'r',
						"executeFunction({z=u*(zMax-zMin)+zMin;theta=v*(thetaMax-thetaMin)+thetaMin;#;x=r*cos(theta);y=r*sin(theta)})",
						"r = 5+sin(z+t)+sin(theta*4)+sin(z/5+t)*3",
						"CylindricalCoordinateSystem_z_dependent",
						"edit(zMax,zMin,thetaMax,thetaMin)"));
		parser.parse(
				"executeFunction({zMin=-10;zMax=10;thetaMin=0;thetaMax=2*pi})")
				.evaluate();

		coordinateSystems
				.add(new CoordinateSystem(
						"Spherical  rho = f(phi,theta)",
						's',
						"executeFunction({phi=u*(phiMax-phiMin)+phiMin;theta=v*(thetaMax-thetaMin)+thetaMin;#;r = rho*sin(phi);x=r*cos(theta);y=r*sin(theta);z = rho*cos(phi)})",
						"rho = 9+sin((10*sin(t/5)+2)*phi+t)",
						"SphericalCoordinateSystem",
						"edit(phiMax,phiMin,thetaMax,thetaMin)"));
		parser.parse("executeFunction({phiMin=0;phiMax=pi})").evaluate();

		coordinateSystems
				.add(new CoordinateSystem(
						"Parametric Surface  (x,y,z) = f(u,v)",
						'p',
						"executeFunction({u=u*(uMax-uMin)+uMin;v=v*(vMax-vMin)+vMin;#})",
						"a=2.7*(1-cos(u)/2);    x= 2.7*cos(u)*(1+sin(u))+a*cos(v)*(u<pi?cos(u):-1);    y=7.2*sin(u)+(u<pi?a*cos(v)*sin(u):0);  z=a*sin(v)",
						"ParametricSurfaceCoordinateSystem",
						"edit(uMax,uMin,vMax,vMin)"));
		parser.parse("executeFunction({uMax=2*pi;uMin=0;vMax=2*pi;vMin=0})")
				.evaluate();

		coordinateSystems
				.add(new CoordinateSystem(
						"Color  color = f(x,y)",
						'o',
						"executeFunction({x=u*(colorXMax-colorXMin)+colorXMin;y=v*(colorYMax-colorYMin)+colorYMin;#;x=(x-colorXMin)/(colorXMax-colorXMin)*20-10;y=(y-colorYMin)/(colorYMax-colorYMin)*20-10;color=(color-colorMin)/(colorMax-colorMin)})",
						"color = sin(x*y/10+t)/2+.5",
						"ColorCoordinateSystem",
						"edit(colorMax,colorMin,colorXMax,colorXMin,colorYMax,colorYMin)",
						Grapher3DConstants.DrawGraphsIn3D + "=false",
						Grapher3DConstants.DrawGraphsIn3D + "=true"));
		parser
				.parse(
						"executeFunction({colorMin=0;colorMax=1;colorXMin=-10;colorXMax=10;colorYMin=-10;colorYMax=10})")
				.evaluate();
/*
		coordinateSystems
				.add(new CoordinateSystem(
						"2D Cartesian  y = f(x)",
						'a',
						"executeFunction({x=u*(xMax-xMin)+xMin;#;x=(x-xMin)/(xMax-xMin)*20-10;y=(y-yMin)/(yMax-yMin)*20-10})",
						"y = sin(x+t)", "Cartesian2DCoordinateSystem",
						"edit(xMax,xMin,yMax,yMin)",
						Grapher3DConstants.DrawGraphsIn3D
								+ "=false; graphUResolutionOutsideCartesian2DCoordinateSystem = "
								+ Grapher3DConstants.GraphResolution_U+";"+ Grapher3DConstants.GraphResolution_U+"=1",
						Grapher3DConstants.DrawGraphsIn3D + "=true;"+Grapher3DConstants.GraphResolution_U+"=graphUResolutionOutsideCartesian2DCoordinateSystem"));
		parser.parse("executeFunction({xMin=-10;xMax=10;yMin=-10;yMax=10})").evaluate();
*/
		// the AWESOME LOOKING klein bottle
		// xBottom = (2.5+1.5*cos(2*v))*cos(u);
		// yBottom=(2.5+1.5*cos(2*v))*sin(u);zBottom = -2.5*sin(2*v); x =
		// v<pi/2?xBottom:0; y= v<pi/2?yBottom:0; z= v<pi/2?zBottom:0
		// xHandle=2-2*cos(v*2-pi)+sin(u); yHandle=cos(u); zHandle= 3*(v*2-pi);
		// x = v<pi?xHandle:0; y= v<pi?yHandle:0; z= v<pi? zHandle:0
		// xTop=2+(2+cos(u))*cos(2*v-2*pi); yTop=sin(u); zTop=
		// 3*pi+(2+cos(u))*sin(2*v-2*pi) ; x = xTop; y=yTop; z=zTop
		// xMid=(2.5+1.5*cos(-v*2+4*pi))*cos(u); yMid=
		// (2.5+1.5*cos(-v*2+4*pi))*sin(u); zMid=3*(-v*2+4*pi); x =xMid; y=yMid;
		// z=zMid

		// scale=.4;r=1.5; u=u+d;a=4*r*(1-cos(u)/2); x=
		// scale*6*cos(u)*(1+sin(u))+scale*a*cos(v)*(u<pi?cos(u):-1);
		// y=16*sin(u)*scale+scale*(u<pi?1:0)*a*sin(u)*cos(v); z=a*sin(v)*scale
		// "scale=.45;r=1.5; a=4*r*(1-cos(u)/2); x=
		// scale*(6*cos(u)*(1+sin(u))+a*cos(v)*(u<pi?cos(u):-1));
		// y=scale*(16*sin(u)+(u<pi?1:0)*a*sin(u)*cos(v)); z=a*sin(v)*scale",

		return coordinateSystems;
	}

	class CoordinateSystemButton extends LogEnabledJRadioButtonMenuItem {
		private static final long serialVersionUID = 3887583792612139407L;

		/**
		 * The coordinate system associated with this button
		 */
		final CoordinateSystem thisSystem;

		public CoordinateSystemButton(CoordinateSystem system) {
			super(system.title, system.alphaNumericName);
			this.setMnemonic(system.mnemonic);
			thisSystem = system;

			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// save the state of the current system, and swap it out for
					// the new one
					if (currentCoordinateSystem != null) {
						// save the state of the current one
						currentCoordinateSystem.saveState();

						// execute the exiting command for the new coordinate
						if (currentCoordinateSystem.initializingCommand != null)
							ExpressionConsoleModel
									.getInstance().executeFunction(currentCoordinateSystem.exitingCommand);
									

					}
					// currentCoordinateSystem is only used for saving states,
					// nothing more
					currentCoordinateSystem = thisSystem;

					// set the actual translator to the new one
					translator.currentTranslator = thisSystem.translationSpecification;

					// execute the initializing command for the new coordinate
					if (thisSystem.initializingCommand != null)
						ExpressionConsoleModel
						.getInstance().executeFunction(currentCoordinateSystem.initializingCommand);

					// set the external (in the text field) function string to
					// the one which is stored in this coordinate system.
					Variable
							.getVariable(
									Grapher3DConstants.Grapher3DFunctionString_external)
							.set(new StringValue(thisSystem.functionString));

				}
			});
		}
	}
}
