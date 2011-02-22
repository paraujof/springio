package issue01methodchaining;

/**
 * Created by IntelliJ IDEA.
 * User: avilches
 * Date: 15/02/11
 * Time: 18:52
 * To change this template use File | Settings | File Templates.
 */
public class MessageTest {


    public static void main(String[] args) {

        new Message().from("chloe.obrian@ctu.com").
                      to("jack.bauer@gmail.com").
                      cc("renee.walker@fbi.gov").
                      subject("Next target coords").
                      text("38º52'42'N 77º02'11'W").
                send();


    }



    public static class Message {

        private String from;
        private String to;
        private String cc;
        private String subject;
        private String text;

        public Message from(String from) {
            this.from = from;
            return this;
        }

        public Message to(String to) {
            this.to = to;
            return this;
        }

        public Message cc(String cc) {
            this.cc = cc;
            return this;
        }

        public Message subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Message text(String text) {
            this.text = text;
            return this;
        }

        public void send() {
            System.out.println("Sending message " +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", cc='" + cc + '\'' +
                    ", subject='" + subject + '\'' +
                    ", text='" + text + '\'');
        }
    }




}
