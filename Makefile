comp:
	export _JAVA_AWT_WM_NONREPARENTING=1
	export GDK_BACKEND=wayland
	export JAVA_TOOL_OPTIONS="-Dsun.java2d.opengl=true"

	mvn compile

	mvn exec:java -Dexec.mainClass="com.exemplo.Main"
