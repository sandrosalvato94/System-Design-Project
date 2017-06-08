--DIAMOND PATH HERE--
prj_project new -name "provaDefinitivaTCL" -impl "impl1" -dev LCMXO2-7000HE-5TG144C -synthesis "lse"
--SOURCE HERE--
--VHDL HERE--
prj_project save
prj_run Export -impl impl1 -forceAll
prj_run Export -impl impl1 -task Bitgen
prj_run PAR -impl impl1 -task IOTiming
pwc_command new
--REPORT HERE--
prj_project close
exit
--END HERE--
