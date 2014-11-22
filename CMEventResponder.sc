CMEventResponder{

	// busses

	var gate;
	var eventSet;
	var <>currentSet;//target resnponder set;
	var <>synths, <>mainSynth, <>secondSynth, <>baseSynth; // where wrapper objects of FM synth will be stored
	var oscSender;
	var <>accelBusses, <>motionBus;

	mapInput{
		arg argName, bus;
		mainSynth.map(argName, bus);
	}
	setAccelBus{
		arg index, val;
		accelBusses[index].value = val;
	}
	setMotionBus{
		arg val;
		motionBus.value = val;
	}
	setCurrentSet{
		arg setNum;
		currentSet = setNum;
		oscSender.sendMsg("/command/responderSet", currentSet);
	}
	sendComment{
		arg comment;
		oscSender.sendMsg("/command/comment", comment );
	}
	sendNext{
		oscSender.sendMsg("/command/next");
	}
	init{
		gate = 0;
		oscSender = NetAddr("127.0.0.1", 50001);
		synths = Array.newClear(3);
		eventSet = Array.fill(5, {Array.newClear(6)});
		accelBusses = Array.fill(3, {Bus.control(Server.default)});
		motionBus = Bus.control(Server.default);

		// event set 0 ... main synth interuption
		eventSet[0][0] = {
			"down: trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(400+1200.rand);
			mainSynth.autoPan(5);
		};

		eventSet[0][1] = {
			"up:trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(2000+2000.rand);
			mainSynth.autoPan(5);
		};

		eventSet[0][2] = {
			"left:trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(400+1200.rand);
			{
				if(gate == 0,{
					mainSynth.tremolo(10, 0, 3);
					mainSynth.sinkFloat(300, 10);
					gate = 1;
					{
						gate = 0;
					}.defer(1);
				});
			}.defer(0.1);
		};

		eventSet[0][3] = {
			"right:trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(2000+2000.rand);
			{
				if(gate == 0,{
					mainSynth.tremolo(10, 0 , 2.0);
					mainSynth.sinkFloat(-300, 10);
					gate = 1;
					{
						gate = 0;
					}.defer(1);
				});
			}.defer(0.1);
		};

		eventSet[0][4] = {
			"slide left:trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(400 + 1200.rand);
		};

		eventSet[0][5] = {
			"slide right:trigger transient".postln;
			this.stopNegativePulse;
			mainSynth.transient(2000 + 2000.rand);
		};

		// event set 1 ["transient + stop"]
		eventSet[1][0] = {
			"empty".postln;
		};
		eventSet[1][1] = {
			"empty".postln;
		};
		eventSet[1][2] = {
			oscSender.sendMsg("/command/next");
		};
		eventSet[1][3] = {
			oscSender.sendMsg("/command/next");
		};
		eventSet[1][4] = {
			"empty".postln;
		};
		eventSet[1][5] = {
			"empty".postln;
		};

		// event set 2
		eventSet[2][0] = {
			secondSynth.sinkFloat(300 +300.rand, 0.1+ 0.3.rand);
		};
		eventSet[2][1] = {
			secondSynth.sinkFloat(-300 -300.rand, 0.2+ 0.2.rand);
		};
		eventSet[2][2] = {
			secondSynth.sinkFloat(200 + 200.rand, 0.1+ 0.3.rand);
		};
		eventSet[2][3] = {
			secondSynth.sinkFloat(-200 - 100.rand, 0.5+ 0.3.rand);
		};
		eventSet[2][4] = {
			secondSynth.sinkFloat(1.0 + 1.0.rand, 0.5+ 0.3.rand);
		};
		eventSet[2][5] = {
			secondSynth.sinkFloat(-1.0 - 1.0.rand, 0.1+ 0.3.rand);
		};


	}
	respond{
		arg type;

		eventSet[currentSet][type].value;
	}
}