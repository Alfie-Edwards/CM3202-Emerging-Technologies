Notes for running:
	Both server and client take an ip address and a port as arguments in either of the following configurations:
		[ipAddress] [port]
		[ipAddress:port]
	The file called "Main" launches a single client and server, mostly for testing purposes
	To launch a single client run CollaborativeCanvas
	To launch a single server run Server
	The file permissions.policy must be in the classpath when either the client or server is run.
	This file applies blanket security permissions. Not the best sollution but I never ran into the need to make these permissions more specific.
	The client is much more complex than the server
	All parts of the client are put together in Client.ApplicationBuilder

Notable features:
	Heavily uses callbacks. See Framework.Remote package for the callbacks implementation.
	You can see other user's cursors move in real-time
	Window is resizable, canvas contents scale with the window
	When drawing, use combinations of Shift, Ctrl, Alt to change the draw mode
	Shift locks the rotation, aligning the shapes to the x-axis
	Alt uses the initial point dragged form as a centre point for the shape
	Ctrl + Shift applies Shift, but the shapes are not confined to being regular. For example, you can draw rectangles.
	Use the colour swatch on the toolbar to change the active colour
	At any time, middle click on a shape to delete it
	When in drawing mode (any shape type), right click any shape to apply the active colour to that shape
	When in movement mode (first option in the toolbar):
		Left click a shape to select it, indicated by the dotted outline (new shapes you draw are automatically selected)
		Left click and drag a shape to drag it
		Right click and drag to rotate the selected shape
		The client actually knows what all users have selected but does not display the information

Known Issues:
	Clients never remove themselves from the user list.
	This is not so bad because all clients own their own user remote object so the object becomes invalid and does not affect anything when the client is closed.

Written and tested using the most recent version of Java 8