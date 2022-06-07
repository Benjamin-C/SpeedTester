package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import commandLine.AdvArguments;
import commandLine.CmdFormater;
import commandLine.CmdOption;
import threading.Job;
import threading.JobPool;

public class SpeedTester {
	
	public static JobPool pool;
	public static AdvArguments args;
	private static CmdFormater cmdFormater;
	private static int jobs;
	
	private static long startTime;
	
	public static void main(String[] argArr) {
		initFormater();
		args = cmdFormater.interpret(argArr);
		
		int threads = 10;
		if(args.hasOption("t")) {
			threads = Integer.parseInt(args.option("t")[0]);
		}
		pool = new JobPool(threads);
		
		jobs = 10*1000*1000;
		if(args.hasOption("j")) {
			jobs = Integer.parseInt(args.option("j")[0]);
		}
		
		long avgTime = 0;
		int numTrials = 10;
		for(int t = 0; t < numTrials; t++) {
			System.out.println("Trial " + (t+1) + "/" + numTrials);
			pool.newJob((Job job) -> {
				startTime = System.nanoTime();
				System.out.println("Started jobs . . .");
				for(int i = 0; i < jobs; i++) {
					pool.newJobBatch(1, (Job job2) -> {
						int a = 0;
						for(int j = 0; j > 0; j++) {
							a++;
							a%=255;
						}
					});
				}
			});
			int n = 0;
			while(n < jobs) {
				List<Job> jbs = pool.getJobsByBatch(1);
				if(jbs == null) continue;
				if(jbs.size() == 0) continue;
				n += jbs.size();
	//			System.out.println(jbs.size() + " jobs just finished. "+((n/jobs)*100)+"% of total.");
			}
			pool.clearDoneJobs();
			long totalTime = System.nanoTime() - startTime;
			if(avgTime == 0) {
				avgTime = totalTime;
			}
			avgTime += totalTime;
			avgTime /= 2;
			System.out.println("Done with "+jobs+" jobs.");
			System.out.println("Took "+totalTime+"ns");
			System.out.println("Formated: " + formatTime(totalTime));
			System.out.println("");
		}
		pool.stop();

		System.out.println("Did "+jobs+" jobs "+numTrials+" times.");
		System.out.println("Took "+avgTime+"ns on average");
		System.out.println("Formated: " + formatTime(avgTime));
		try {
			FileWriter myWriter = new FileWriter("time.log");
			myWriter.write("Did "+jobs+" jobs "+numTrials+" times.\n");
			myWriter.write("Took "+avgTime+"ns on average\n");
			myWriter.write("Formated: " + formatTime(avgTime));
			myWriter.close();
			System.out.println("Successfully wrote to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void initFormater() {
		cmdFormater = new CmdFormater();
		cmdFormater.setOption(new CmdOption("t",1));
		cmdFormater.setOption(new CmdOption("j",1));
	}
	
	private static String formatTime(long nanoTime) {
		long nano = nanoTime % 1000000000;
		long seconds = nanoTime / 1000000000;
		long minutes = seconds / 60;
		seconds %= 60;
		return minutes+":"+seconds+"."+nano;
	}
}
