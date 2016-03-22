import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class bbst {
	RBTree rbTree;
	bbst() {
		rbTree = new RBTree();
	}
	
	public void increase(int key, int val) {
		rbTree.insert(key, val);
	}
	
	public void reduce(int key, int val) {
		rbTree.reduce(key,val);
	}
	
	public int count(int key) {
		return rbTree.get(key);
	}
	
	public int inRange(int low, int high) {
		return rbTree.range(low, high);
	}
	
	public int[] next(int key) {
		return rbTree.next(key);
	}
	
	public int[] previous(int key) {
		return rbTree.previous(key);
	}
	
	public boolean check() {
		return rbTree.check();
	}
	public static void main(String[] args) {
//		if(args.length != 1) {
//			System.out.println("Invalid inputs");
//			return;
//		}
		//storing input filename
		String fileName = args[0];
		String fileName2 = args[1];
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		
		bbst test = new bbst();
		try {
			String currLine = null;
			// load the file into bufferedReader
			br1 = new BufferedReader(new FileReader(fileName));
			//read # of key value pairs
			int n = Integer.valueOf(br1.readLine());
			
			while((currLine = br1.readLine()) != null) {
				String[] nums = currLine.split(" ");
				// if (key,value) format is not present ignore that line
				if(nums.length == 2) {
					int a = Integer.valueOf(nums[0]);
					int b = Integer.valueOf(nums[1]);
					//start insert the key-value as node to RBTree
					test.increase(a, b);
					//System.out.println("value of "+ a+ " "+test.count(a));
				}
			}
//			System.out.println("*****printing tree******");
//			for(int i : test.rbTree.keys(0, 1000))
//				System.out.print(i+" ");
			//br2 = new BufferedReader(new InputStreamReader(System.in));
			br2 = new BufferedReader(new FileReader(fileName2));
			while(true) {
				currLine = br2.readLine();
				String[] commands = currLine.split(" ");
				String command = commands[0];
				
				switch (command) {
					case "increase":
						test.increase(Integer.valueOf(commands[1]), Integer.valueOf(commands[2]));
						System.out.println(test.count(Integer.valueOf(commands[1])));
						break;
					case "reduce":
						test.reduce(Integer.valueOf(commands[1]), Integer.valueOf(commands[2]));
						System.out.println(test.count(Integer.valueOf(commands[1])));
						break;
					case "previous":
						int[] res = test.previous(Integer.valueOf(commands[1]));
						System.out.println(res[0]+ " "+ res[1]);
						break;
					case "next":
						res = test.next(Integer.valueOf(commands[1]));
						System.out.println(res[0]+" "+res[1]);
						break;
					case "count":
						System.out.println(test.count(Integer.valueOf(commands[1])));
						break;
					case "inrange":
						System.out.println(test.inRange(Integer.valueOf(commands[1]),Integer.valueOf(commands[2])));
						break;
					case "quit":
//						System.out.println("*****printing tree******");
//						for(int i : test.rbTree.keys(0, 1000))
//							System.out.print(i+" ");
						return;
					default:
						break;
				}
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br1 != null) br1.close();
				if(br1 != null) br2.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}
}
