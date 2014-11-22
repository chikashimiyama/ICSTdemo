CMGUI {
	var win;
	var sliders;
	var label;
	var histerisis;
	var nextButton,rewindButton;
	var statusField, responderField;
	var motion;
	var qlist;

	init{
		qlist = CMQlist.new.init;

		win = Window("sensor", Rect(10,100,320, 240));
		sliders = Array.new;
		label = ["accelX", "accelY", "accelZ", "gyroX", "gyroY", "gyroZ"];
		histerisis = Array.fill(6, 0);
		win.view.decorator = FlowLayout( win.view.bounds, 10@10, 20@5);
		statusField = TextField(win, 300@30 );
		responderField = EZNumber(win, 150@30, "resp" );

		nextButton = Button(win, 50@30).states_([["next"]]).action_({
			"qlist advanced".postln;
			qlist.next;
		});
		rewindButton = Button(win, 50@30).states_([["rewind"]]).action_({
			qlist.init;
		});


		6.do{
			arg num;
			sliders = sliders.add(EZSlider(win, 300@20, label.at(num)));
		};
		OSCFunc.removeFunc();

		OSCFunc.newMatching({
			{
				"next button performed".postln;
				nextButton.valueAction_(0);
			}.defer;

		}, "/command/next", recvPort:50001);

		OSCFunc.newMatching({
			arg msg;
			{
				responderField.value = msg.at(1);
			}.defer;
		}, "/command/qlist.responderSet", recvPort:50001);

		OSCFunc.newMatching({
			arg msg;
			{
				statusField.value = msg.at(1);
			}.defer;
		}, "/command/comment", recvPort:50001);

		OSCFunc.newMatching({
			|msg, time, addr, recvPort|
			{
				sliders[0].value = (msg.at(1)-0.3) * 2.5;
				sliders[1].value = (msg.at(2)-0.3) * 2.5;
				sliders[2].value = (msg.at(3)-0.3) * 2.5;
				qlist.responder.setAccelBus(0, sliders[0].value);
				qlist.responder.setAccelBus(1, sliders[1].value);
				qlist.responder.setAccelBus(2, sliders[2].value);


			}.defer;
		}, "/icst/mpu/accel", recvPort:50000);

		OSCFunc.newMatching({
			|msg, time, addr, recvPort|
			{
				sliders[3].value = (msg.at(1) - 0.475) * 20.0;
				sliders[4].value = (msg.at(2) - 0.475) * 20.0;
				sliders[5].value = (msg.at(3) - 0.475) * 20.0;
				motion = 0.0;
				motion = motion + (sliders[3].value - 0.5).abs;
				motion = motion + (sliders[4].value - 0.5).abs;
				motion = motion + (sliders[5].value - 0.5).abs;
				qlist.responder.setMotionBus(motion);

				if(sliders[3].value < 0.35,{
					if(histerisis[0] == 0 ,{
						"down".postln;
						qlist.responder.respond(0);
						histerisis[0] = 1;
						{
							histerisis[0] = 0;
						}.defer(1.0);
					});
				});
				if(sliders[3].value > 0.65,{
					if(histerisis[1] == 0 ,{
						"up".postln;
						qlist.responder.respond(1);
						histerisis[1] = 1;
						{
							histerisis[1] = 0;
						}.defer(1.0);
					});
				});
				if(sliders[4].value < 0.35,{
					if(histerisis[2] == 0 ,{
						"left".postln;
						qlist.responder.respond(2);
						histerisis[2] = 1;
						{
							histerisis[2] = 0;
						}.defer(1.0);
					});
				});
				if(sliders[4].value > 0.65,{
					if(histerisis[3] == 0 ,{
						"right".postln;
						qlist.responder.respond(3);
						histerisis[3] = 1;
						{
							histerisis[3] = 0;
						}.defer(1.0);
					});
				});
				if(sliders[5].value < 0.35,{
					if(histerisis[4] == 0 ,{
						"right slide".postln;
						qlist.responder.respond(4);
						histerisis[4] = 1;
						{
							histerisis[4] = 0;
						}.defer(1.0);
					});
				});
				if(sliders[5].value > 0.65,{
					if(histerisis[5] == 0 ,{
						"left slide".postln;
						qlist.responder.respond(5);
						histerisis[5] = 1;
						{
							histerisis[5] = 0;
						}.defer(1.0);
					});
				});

			}.defer;
		}, "/icst/mpu/gyro", recvPort:50000);


		win.front;
	}
}
