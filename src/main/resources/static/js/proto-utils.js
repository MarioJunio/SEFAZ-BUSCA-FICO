function callDesktopMOS(ca) {
	var w = (window.parent) ? window.parent : window;
	w.location.assign('testJava:ca=' + ca);
}