cd "C:/lscc/diamond/3.9_x64/bin/nt64"
prj_project new -name "provaDefinitivaTCL" -impl "impl1" -dev LCMXO2-7000HE-5TG144C -synthesis "lse"
file mkdir "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source"
--VHDL HERE--
file copy -force -- "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/Tmp/TopLevelEntity.vhd" "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/BUFFER_DATA.vhd" "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/constants.vhd" "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/IP_Adder.vhd" "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/IP_Dummy.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source"
file copy -force -- "C:/Users/UTENTE/workspace/System-Design-Project/src/polito/sdp2017/Tests/IPManager_HardwareGroup.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source"
prj_src add "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/TopLevelEntity.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/BUFFER_DATA.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/constants.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/IP_Adder.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/IP_Dummy.vhd" "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/source/IPManager_HardwareGroup.vhd"
prj_project save
prj_run Export -impl impl1 -forceAll
prj_run Export -impl impl1 -task Bitgen
prj_run PAR -impl impl1 -task IOTiming
pwc_command new
pwc_writereport html -file "C:/lscc/diamond/3.9_x64/bin/nt64/impl1/report.html"
prj_project close
exit
--END HERE--
