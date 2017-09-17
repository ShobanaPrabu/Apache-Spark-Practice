package mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vdokku on 6/30/2017.
 */
public class MutualFriendFinder {

    public static class TestMapper extends Mapper<Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) {

        }
    }

    public static class TestReducer extends Reducer<Text, Text, Text, Text> {

    }


    public static class MutualFriendFinderMapper extends Mapper<Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String inputline = value.toString();
            String[] inputlinesplit = inputline.split("->"); //Split each line based on -> symbol
            String person = inputlinesplit[0].trim(); // first part of split is the person id
            String[] friends = inputlinesplit[1].split(" "); // this array contains the list of friends
            String mapkey = "";
            for (int i = 0; i < friends.length; i++) {
                if (friends[i] != null && !friends[i].isEmpty()) {
                    //compare both the person and friend and combine accordingly to maintain alphabetic order
                    if (person.compareTo(friends[i].trim()) < 0) {
                        mapkey = person + " " + friends[i].trim();
                    } else if (person.compareTo(friends[i].trim()) > 0) {
                        mapkey = friends[i].trim() + " " + person;
                    }
                    context.write(new Text(mapkey), new Text(inputlinesplit[1])); //mapper output
                }
            }
        }
    }

    public static class MutualFriendFinderReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            ArrayList<String> valuelist = new ArrayList<String>();
            valuelist.clear();
            for (Text val : values) {
                valuelist.add(val.toString()); //get list of values associated with a key
            }
            ArrayList<String> friendlist = new ArrayList<String>();
            for (int i = 0; i < valuelist.size(); i++) {
                String[] friends = valuelist.get(i).split(" "); //split each value list into string array of friends
                for (int j = 0; j < friends.length; j++) {
                    friendlist.add(friends[j].trim()); //add each friend to friendlist
                }
            }
            HashSet<String> friendset = new HashSet<String>();
            String mutualfriends = "";
            for (int i = 0; i < friendlist.size(); i++) {
                if (friendset.contains(friendlist.get(i))) {
                    mutualfriends += " " + friendlist.get(i); //check if the friend is in friendset if so add to mutualfriends else add to friendset
                } else {
                    friendset.add(friendlist.get(i));
                }
            }
            context.write(key, new Text(" are mutual/common friends with " + mutualfriends)); //reducer output
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MutualFriendFinder");
        job.setJarByClass(MutualFriendFinder.class);
        job.setMapperClass(MutualFriendFinderMapper.class);
        job.setReducerClass(MutualFriendFinderReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
