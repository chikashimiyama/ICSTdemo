CMFMSoundObject{
	var synth;
	var currentAmp;
	var currentFreq;
	init{
		arg carrierFreqOffset = 220, modFreqOffset = 220 , modDepthOffset = 1.0, amp = 0.0, pan = 0.0, revAmp = 0.1, freqShiftAmp = 0.0;
		synth = Synth("FM", [
			"carrierFreqOffset", carrierFreqOffset,
			"modFreqOffset", modFreqOffset,
			"modDepthOffset", modDepthOffset,
			"amp", amp, "pan", pan, "revAmp", revAmp, "freqShiftAmp", freqShiftAmp], ~fmGroup);
		currentAmp = amp;
		currentFreq = carrierFreqOffset;

	}
	mapInput{
		arg argName, bus;
		synth.map(argName, bus);
	}
	fadeIn{
		arg amp, dur, curve;
		var ampBus = Bus.control(Server.default);
		synth.map("amp", ampBus);
		Synth("Ramp", ["outBus", ampBus, "origin", currentAmp, "dest", amp, "dur", dur, "curve", curve], ~lfoGroup);
		currentAmp = amp;
	}
	fadeOut{
		arg dur, curve;
		var ampBus = Bus.control(Server.default);
		synth.map("amp", ampBus);
		Synth("Ramp", ["outBus", ampBus, "origin", currentAmp, "dest", 0, "dur", dur, "curve", curve], ~lfoGroup);
	}
	tremolo{
		arg initFreq = 10, destFreq = 0, dur = 10;
		var ampBus = Bus.audio(Server.default);
		synth.map("negAmp", ampBus);
		Synth("NegativePulse", ["outBus", ampBus, "initFreq", 10, "destFreq", 0, "dur", dur], ~lfoGroup);
	}
	gliss{
		arg dest = -100, dur = 10;
		var freqBus = Bus.control(Server.default);
		synth.map("carrierFreqOffset", freqBus);
		Synth("Ramp", [ "outBus", freqBus, "origin", currentFreq,　"dest", currentFreq+dest,　"dur", dur, "curve", 1　], ~lfoGroup); // gliis down
	}
	autoPan{
		arg dur = 10;
		var panBus = Bus.control(Server.default);
		synth.map("pan", panBus);
		Synth("AutoPan", ["outBus", panBus, "dur", dur ], ~lfoGroup);
	}
	transient{
		var freqBus, ampBus, freq = 3000;

		freqBus = Bus.audio(Server.default);
		ampBus = Bus.control(Server.default);
		synth.map("transientAmp", ampBus);
		synth.map("transientFreq", freqBus);
		Synth("Transient", ["outBus", freqBus, "ampBus", ampBus, "freq", freq], ~lfoGroup);

	}
	movePan{
		arg origin, dest, dur;
		var panBus = Bus.control(Server.default);
		synth.map("pan", panBus);
		Synth("Ramp",["outBus", panBus, "origin", origin, "dest", dest, "dur", dur], ~lfoGroup);
	}
	modFreqRamp{
		arg origin, dest, dur;
		var modFreqBus = Bus.control(Server.default);
		synth.map("modFreqOffset", modFreqBus);
		Synth("Ramp", ["outBus", modFreqBus, "origin", origin, "dest", dest, "dur", dur], ~lfoGroup);
	}
	modDepthRamp{
		arg origin, dest, dur;
		var modDepthBus = Bus.control(Server.default);
		synth.map("modDepthOffset", modDepthBus);
		Synth("Ramp", ["outBus", modDepthBus, "origin", origin, "dest", dest, "dur", dur], ~lfoGroup);
	}
	freqShiftRamp{
		arg origin, dest, dur;
		var freqShiftBus = Bus.control(Server.default);
		synth.map("freqShiftFreq", freqShiftBus);
		Synth("Ramp", ["outBus", freqShiftBus, "origin", origin, "dest", dest, "dur", dur], ~lfoGroup);
	}
	sinkFloat{
		arg freq, dur;
		var carrierFreqBus = Bus.control(Server.default);
		synth.map("carrierFreqOffset", carrierFreqBus);
		Synth("SinkFloat", ["outBus", carrierFreqBus, "freq", freq, "dur", dur], ~lfoGroup);
	}
	sinkFloatModFreq{
		arg freq, dur;
		var carrierFreqBus = Bus.control(Server.default);
		synth.map("modFreq", carrierFreqBus);
		Synth("SinkFloat", ["outBus", carrierFreqBus, "freq", freq, "dur", dur], ~lfoGroup);
	}
	sinkFloatModDepth{
		arg freq, dur;
		var carrierFreqBus = Bus.control(Server.default);
		synth.map("carrierFreqOffset", carrierFreqBus);
		Synth("SinkFloat", ["outBus", carrierFreqBus, "freq", freq, "dur", dur], ~lfoGroup);
	}
	staccato{
		arg attack = 0.05, release = 0.1;
		var ampBus = Bus.control(Server.default);
		synth.map("amp", ampBus);
		Synth("Staccato", ["outBus", ampBus, "attack", attack, "release", release], ~lfoGroup);
	}

}




