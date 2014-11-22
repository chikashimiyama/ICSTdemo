
CMQlist{
	var qlist;
	var	<>responder;

	next{
		qlist.next;
	}
	rewind{
		qlist.rewind;
	}
	init{
		~lfoGroup = Group.new;
		~fmGroup = Group.tail(Server.default);
		responder = CMEventResponder.new.init;
		qlist = Routine{
			var buses;
			var bassAmp;
			var drone, droneAmp;
			var lfoGroup, fmGroup;



			/************************* cue 1 *************************/
			responder.setCurrentSet(1);
			responder.sendComment("1: up/down gesture to advance");



			// first tone
			responder.synths[0] = CMFMSoundObject.new.init;
			responder.synths[0].fadeIn(0.2,5,5);
			responder.synths[0].tremolo(10, 0, 10);
			responder.synths[0].autoPan(10);

			2.yield;
			/************************* cue 2 *************************/
			"cue2".postln;
			responder.sendComment("2: up/down gesture to advance");

			// stop first gesture
			responder.synths[0].fadeOut(10, -5);
			responder.synths[0].gliss(-40, 10);

			// tone
			responder.synths[1] = CMFMSoundObject.new.init(250, 252);
			responder.synths[1].fadeIn(0.2, 6, 6);

			responder.synths[1].transient;
			responder.synths[1].autoPan(6);

			3.yield;
			/************************* cue 3 *************************/
			"cue3".postln;
			responder.sendComment("3: up/down gesture to advance");

			// tone 1

			// tone 2
			responder.synths[1].tremolo(10,0,10);
			responder.synths[1].fadeOut(10);
			responder.synths[1].gliss(-30, 10);


			// tone 3
			responder.synths[2] = CMFMSoundObject.new.init(345, 497);
			responder.synths[2].transient;
			responder.synths[2].fadeIn(0.1, 10);
			responder.synths[2].autoPan(10);

			4.yield;
			/************************* cue 4 *************************/
			"cue4".postln;
			responder.sendComment("4: up/down gesture to advance");

			// tone 1
			responder.synths[0] = CMFMSoundObject.new.init(111, 120, 1.5);
			responder.synths[0].transient;
			responder.synths[0].fadeIn(0.1, 6);
			responder.synths[0].tremolo(20,3,10);
			responder.synths[0].gliss(45, 10);

			// tone 2


			// tone 3
			responder.synths[2].fadeOut(10);

			// metal

			{
				var fm, ampBus, metal, routine;
				ampBus = Bus.control(Server.default);
				metal = {
					fm = Synth("FM",
						[
							"carrierFreqOffset", 355 + {42.rand},
							"modFreq", 452 + {722.rand},
							"modDepthOffset", 0.9,
							"pan", {1.0.rand2},
							"revAmp", 0.5
					]);
					fm.map("amp", ampBus);
					Synth("Staccato", ["outBus", ampBus ]);
				};
				routine = Routine({
					4.do({
						metal.value;
						(0.2 + 3.7.rand).wait;
					});

				});
				routine.play;
			}.defer(5);


			5.yield;
			/************************* cue 5 *************************/
			"cue5".postln;
			responder.sendComment("5: up/down gesture to advance");

			// tone 1
			responder.synths[0].fadeOut(10,-5);

			// tone 2
			responder.synths[1] = CMFMSoundObject.new.init(400, 300, 0.7);
			responder.synths[1].transient;
			responder.synths[1].fadeIn(0.2, 7, 2);
			responder.synths[1].autoPan(10);
			responder.synths[1].gliss(30, 10);

			// tone 3

			6.yield;
			/************************* cue 6 *************************/
			"cue6".postln;
			responder.sendComment("6: up/down gesture to advance");
			// tone 1
			{
				responder.synths[0] = CMFMSoundObject.new.init(42+32.rand, 700+622.rand, 2, revAmp:0.2);
				responder.synths[0].staccato(5+3.0.rand, 5.0+3.1.rand);
			}.defer(5);

			// tone 2
			responder.synths[1].fadeOut(10, -3);

			// tone 3
			responder.synths[2] = CMFMSoundObject.new.init(300, 220, 1.2);
			responder.synths[2].transient;
			responder.synths[2].fadeIn(0.2, 6, 6);
			responder.synths[2].tremolo(5, 0, 6);
			responder.synths[2].gliss(40, 10);

			7.yield;
			/************************* cue 7 *************************/
			"cue7".postln;
			responder.sendComment("7: up/down gesture to advance");

			// tone 1
			responder.synths[0] = CMFMSoundObject.new.init(12, 420, 1.5);
			responder.synths[0].transient;
			responder.synths[0].gliss(200, 10);
			responder.synths[0].autoPan(6);
			responder.synths[0].fadeIn(0.1, 7, 6);

			// tone 2

			// tone 3
			responder.synths[2].fadeOut(10);

			8.yield;
			/************************* cue 8 *************************/
			/* main tone */
			"cue8".postln;
			responder.sendComment("8: up/down gesture to advance");
			responder.setCurrentSet(0);

			// tone 1
			responder.synths[0].fadeOut(8, -5);

			// tone 2

			// main tone
			responder.mainSynth = CMFMSoundObject.new.init(330, 166, 0.8, 0.2, revAmp:0.1);
			responder.mainSynth.transient;
			responder.mainSynth.mapInput("carrierFreqMapped" , responder.accelBusses[1]);
			responder.mainSynth.mapInput("modDepthMapped" , responder.accelBusses[2]);
			responder.mainSynth.mapInput("motionAmp", responder.motionBus);

			// automatic advancing
			{
				responder.sendNext;
			}.defer(10);
			9.yield;
			/************************* cue 9 *************************/
			"cue9".postln;
			responder.sendComment("9: up/down gesture to advance");

			responder.synths[0] = CMFMSoundObject.new.init(220, 50, 6, 0.2,revAmp:0.2);
			responder.synths[0].movePan(-1, 1, 5);
			responder.synths[0].modFreqRamp(440, 0, 9);
			responder.synths[0].modDepthRamp(0, 20, 9);
			responder.synths[0].staccato(3, 6);

			{
				responder.sendNext;
			}.defer(8);
			10.yield;
			/************************* cue 10 *************************/
			"cue10".postln;
			responder.sendComment("10: up/down gesture to advance");

			responder.synths[1] = CMFMSoundObject.new.init(115, 332, 1, 0.2, revAmp:0.1, freqShiftAmp:0.5);
			responder.synths[1].staccato(5, 5);
			responder.synths[1].modFreqRamp(330, 50, 7);
			responder.synths[1].modDepthRamp(4, 1, 9);
			responder.synths[1].freqShiftRamp(7, 0, 12);

			{
				responder.sendNext;
			}.defer(14);
			11.yield;
			/************************* cue 11 *************************/
			"cue11".postln;
			responder.sendComment("11: up/down gesture to advance");

			responder.synths[2] = CMFMSoundObject.new.init(220, 50, 6, 0.2, revAmp:0.2);
			responder.synths[2].modFreqRamp(440, 0, 6);
			responder.synths[2].modDepthRamp(2, 12, 9);
			responder.synths[2].movePan(-1, 1, 5);
			responder.synths[2].staccato(3, 6);

			{
				responder.sendNext;
			}.defer(5);
			12.yield;
			/************************* cue 12 *************************/
			"cue12".postln;
			responder.sendComment("12: up/down gesture to advance");

			responder.synths[0] = CMFMSoundObject.new.init(200, 101, 5, 0.2, freqShiftAmp:0.5);
			responder.synths[0].tremolo(2, 40, 10);
			responder.synths[0].autoPan(10);
			responder.synths[0].modDepthRamp(12, 3, 10);
			responder.synths[0].staccato(3, 7 );
			responder.synths[0].gliss(120, 10);
			responder.synths[0].freqShiftRamp(0, 6, 12);


			// main synth //
			responder.mainSynth.gliss(-200, 30);
			{
				responder.sendNext;
			}.defer(11);
			13.yield;
			/************************* cue 13 *************************/
			/* finale */
			"cue13".postln;

			responder.sendComment("13: up/down gesture to advance");
			responder.mainSynth.fadeOut(30);

			{
				var fm, ampBus, metal, routine;
				ampBus = Bus.control(Server.default);
				metal = {
					fm = Synth("FM",
						[
							"carrierFreqOffset", 42 + {42.rand},
							"modFreq", 200 + {722.rand},
							"modDepthOffset", 3,
							"revAmp", 0.5
					]);
					fm.map("amp", ampBus);
					Synth("Staccato", ["outBus", ampBus ]);
				};
				routine = Routine({
					4.do({
						metal.value;
						(0.2 + 1.7.rand).wait;
					});

				});
				routine.play;
			}.value;
			{
				responder.sendNext;
			}.defer(8);
			14.yield;
			/************************* cue 14 *************************/
			"cue14".postln;
			responder.sendComment("14: up/down gesture to advance");
			responder.setCurrentSet(2);

			// second synth
			responder.secondSynth = CMFMSoundObject.new.init(100, 200, 10, revAmp:0.03);
			responder.secondSynth.fadeIn(0.2, 10, 5);
			responder.secondSynth.transient;

			// tone 1
			responder.synths[0] = CMFMSoundObject.new.init(42+32.rand, 700+622.rand, 2, revAmp:0.2);
			responder.synths[0].staccato(3+3.0.rand, 0.1+0.1.rand);

			{
				responder.sendNext;
			}.defer(12);
			15.yield;
			/************************* cue 15 *************************/
			"cue15".postln;
			responder.sendComment("15: up/down gesture to advance");

			// base synth
			responder.baseSynth = CMFMSoundObject.new.init(6, 20, 6, 0);
			responder.baseSynth.fadeIn(0.3, 20, 5);



			{
				var metal, routine;
				metal = {
					var fm = CMFMSoundObject.new.init(
						25 + {33.rand}.value,
						200 + {555.rand}.value,
						2,
						revAmp:0.2,
						pan: {1.0.rand2}.value);
					fm.staccato({0.1}.rand+0.05, 0.1);
				};
				routine = Routine({
					5.do({
						metal.value;
						(0.2 + 1.3.rand).wait;
					});

				});
				routine.play;
			}.value;

			// tone 2
			responder.synths[1] = CMFMSoundObject.new.init(42+32.rand, 700 + 622.rand, 2, revAmp: 0.5);
			responder.synths[1].staccato(6.0+3.0.rand, 0.1+0.1.rand);

			{
				responder.sendNext;
			}.defer(10);
			16.yield;
			/************************* cue 16 *************************/
			"cue16".postln;
			responder.sendComment("16: up/down gesture to advance");



			{
				var metal, routine;
				metal = {
					var fm = CMFMSoundObject.new.init(
						25 + {33.rand}.value,
						111 + {555.rand},
						3,
						revAmp:0.3,
						pan:{1.0.rand2}.value);
					fm.staccato({0.1.rand} + 0.1, {0.3.rand}+0.1);
				};
				routine = Routine({
					6.do({
						metal.value;
						(0.2 + 0.8.rand).wait;
					});
					4.do({
						metal.value;
						(0.2 + 0.6.rand).wait;
					});
					3.do({
						metal.value;
						(0.6 + 2.0.rand).wait;
					});

				});
				routine.play;
			}.value;
			{
				responder.sendNext;
			}.defer(12);
			17.yield;
			/************************* cue 17 *************************/
			"cue17".postln;
			responder.sendComment("17: up/down gesture to advance");

			responder.secondSynth.fadeOut(30, -5);
			responder.secondSynth.gliss(100, 30);
			{
				responder.sendNext;
			}.defer(10);
			18.yield;
			/************************* cue 18 *************************/
			"cue18".postln;
			responder.sendComment("18: up/down gesture to advance");

						{
				var metal, routine;
				metal = {
					var fm = CMFMSoundObject.new.init(
						66 + {33.rand}.value,
						111 + {555.rand},
						5,
						revAmp:0.3,
						pan:{1.0.rand2}.value);
					fm.staccato({0.3.rand} + 0.1, {0.3.rand}+0.1);
				};
				routine = Routine({
					3.do({
						metal.value;
						(4.3 + 2.0.rand).wait;
					});

				});
				routine.play;
			}.value;
			// base synth
			responder.baseSynth.fadeOut(8);
		}
	}


	stop{


	}
}