package a.team.works.u22.hal.u22teama;

public  interface HttpPostListener {
    abstract public void postCompletion(byte[] response);
    abstract public void postFialure();
}
