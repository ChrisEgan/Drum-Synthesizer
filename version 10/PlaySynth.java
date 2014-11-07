//package com.jsyn.examples;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.instruments.SubtractiveSynthVoice;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitVoice;
import com.softsynth.shared.time.TimeStamp;
 import com.jsyn.unitgen.*;
/**
 * Play notes using timestamped noteOn and noteOff methods of the UnitVoice.
 * 
 * @author Phil Burk (C) 2009 Mobileer Inc
 * 
 */
public class PlaySynth
{
 Synthesizer synth;
 UnitGenerator ugen;
 UnitVoice voice;
 LineOut lineOut;
 WhiteNoise noise;
 void play(double volume, double pitch, double cutoff)
 {
  // Create a context for the synthesizer.
  synth = JSyn.createSynthesizer();
  // Set output latency to 123 msec because this is not an interactive app. noise
  synth.getAudioDeviceManager().setSuggestedOutputLatency( 0.040 );
  
  // Add a tone generator.
  
  //synth.add( noise  = new WhiteNoise());
 // voice = (UnitVoice) noise;
 
  synth.add( ugen = new SineOscillator() );
  voice = (UnitVoice) ugen;
  
  // Add an output mixer.
  synth.add( lineOut = new LineOut() );
  
  
  // Connect the oscillator to the left and right audio output.
   voice.getOutput().connect( 0, lineOut.input, 0 );
   voice.getOutput().connect( 0, lineOut.input, 1 );
  
  // Start synthesizer using default stereo output at 44100 Hz.
  synth.start();
 
  // Get synthesizer time in seconds.
  double timeNow = synth.getCurrentTime();

  // Advance to a near future time so we have a clean start.
  TimeStamp timeStamp = new TimeStamp(timeNow + 0.5);

  // We only need to start the LineOut. It will pull data from the
  // oscillator.
  synth.startUnit( lineOut, timeStamp );
  
  // Schedule a note on and off.
   
  double duration = 1.4;
  double onTime = 1.0;
  //set pitch and volume
  voice.noteOn( pitch, volume/100, timeStamp );
  //amp envelope is a function of amplitude(volume over time 
  //between note on and note off. 
   voice.noteOff( timeStamp.makeRelative( onTime ) );

  

  // Sleep while the song is being generated in the background thread.
  try
  {
   System.out.println("Sleep while synthesizing.");
   synth.sleepUntil( timeStamp.getTime() + 2.0 );
   System.out.println("Woke up...");
  } catch( InterruptedException e )
  {
   e.printStackTrace();
  }

  // Stop everything.
  synth.stop();
 }

// public static void main( String[] args )
// {
//  new PlayNotes().test();
// }
}
