(
Server.default.options.numControlBusChannels = 8192;
Server.default.options.memSize = 655360;
Server.default.waitForBoot({
	c = CMGUI.new.init;
});
Server.default.boot;
)

SynthDef("DecayLine",{
	arg outBus, dur = 5.0;
	var env = Env([0.5,0.1],[dur],[-1]);
	Out.kr(outBus, EnvGen.kr(env, doneAction:2));
}).load;

SynthDef("Transient",{
	arg outBus, ampBus, freq = 3000, depth = 3000, initAmp = 0.3, dur = 0.5, curve = -10, fadeOutDur = 7.0;
	var freqSweep = Env([freq, 0], [dur],[curve]);
	var depthSweep = Env([depth, 0], [dur],[curve]);
	var dim = Env([initAmp, 0.0], [fadeOutDur], [-0.5]);
	var transient = LFNoise2.ar(EnvGen.kr(freqSweep), EnvGen.kr(depthSweep));
	Out.ar(outBus, transient);
	Out.kr(ampBus , EnvGen.kr(dim, doneAction:2));
}).load;

SynthDef("SinkFloat", {
	arg outBus, freq = 50, dur = 4.0;
	Out.kr(outBus, EnvGen.kr(Env([0, freq, 0],[0.05, dur],[0,4]),doneAction:2 ));
}).load;

SynthDef("AutoPan",{
	arg outBus, dur = 10;
	var env = Env([1, 0], [dur],[-1]);
	var control = SinOsc.kr(EnvGen.kr(env, doneAction:2)*8, 0, EnvGen.kr(env, doneAction:2));
	Out.kr(outBus, control);
}).load;

SynthDef("NegativePulse", {
	arg outBus = 0, dur = 10.0, initFreq = 20.0, destFreq = 3.0;
	var pulse = Impulse.kr(Line.kr(initFreq, destFreq, dur, doneAction:2));
	pulse = EnvGen.ar(Env([0,1,0],[0.01,0.02]),pulse);
	Out.ar(outBus, pulse );
}).load;

SynthDef("Staccato",{
	arg outBus = 0, attack = 0.05, release = 0.1;
	var env = Env([0,0.2,0],[attack, release],[5,-5]);
	Out.kr(outBus, EnvGen.kr(env, doneAction:2));
}).load;

SynthDef("Ramp",{
	arg outBus, origin, dest, dur, curve = 0;
	var ramp = EnvGen.kr(Env([origin, dest],[dur],[curve]), doneAction: 2);
	Out.kr(outBus, ramp);
}).load;

SynthDef("FM", {
	arg amp = 0.0, negAmp = 0.0, transientAmp = 0.0, motionAmp = 0.0, motionAmpScale = 5.0,
	carrierFreqOffset = 220, carrierFreqMapped =0.0, carrierFreqScale = 220, carrierFreqSink, transientFreq = 0.0,
	modFreqOffset = 220, modFreqMapped = 0.0, modFreqScale = 220, modFreqSink,
	modDepthOffset = 1.0, modDepthMapped = 0.0, modDepthScale = 20.0, modDepthSink,
	freqShiftFreq = 0.0 , freqShiftAmp = 0.0,
	pan = 0.0, revAmp = 0.03;

	var car = K2A.ar(Lag.kr(carrierFreqMapped * carrierFreqScale, 0.5)+ carrierFreqOffset + LFNoise2.kr(0.6, 5) + carrierFreqSink) + transientFreq;
	var mfq = Lag.kr(modFreqMapped * modFreqScale, 0.5) + modFreqSink + modFreqOffset;
	var mdp = Lag.kr(modDepthMapped * modDepthScale, 0.5) + modDepthSink + modDepthOffset;
	var sin = SinOsc.ar(car, SinOsc.ar(mfq, 0, mdp ));

	var amped = sin * (amp + transientAmp + (Lag.kr(motionAmp, 0.5)*motionAmpScale));
	var negatived = amped * (1.0 - negAmp);
	var snd = FreqShift.ar(negatived, 0.0 , SinOsc.ar(freqShiftFreq, 0, 3.14)) * freqShiftAmp + negatived;
	var reverb = GVerb.ar(snd, revtime:3, drylevel:0) * revAmp;
	DetectSilence.ar(snd, 0.00005, 3, doneAction:2); // for security
	Out.ar(0, Pan2.ar(snd ,pan) + reverb);
}).load;


)
