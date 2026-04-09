package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.*;

import org.cloudbus.cloudsim.core.CloudSim;

import org.cloudbus.cloudsim.provisioners.*;

import java.util.*;

public class mounika1  {

    public static void main(String[] args) throws Exception {



        CloudSim.init(1, Calendar.getInstance(), false);



        // Datacenter (1 host, 1 PE)

        List<Pe> pe = Arrays.asList(new Pe(0, new PeProvisionerSimple(1000)));

        Host host = new Host(0, new RamProvisionerSimple(2048),

                new BwProvisionerSimple(10000), 1000000,

                pe, new VmSchedulerTimeShared(pe));



        Datacenter dc = new Datacenter("DC",

                new DatacenterCharacteristics("x86","Linux","Xen",

                        Arrays.asList(host),10,3,0.05,0.1,0.1),

                new VmAllocationPolicySimple(Arrays.asList(host)),

                new LinkedList<>(), 0);



        // Broker + VM

        DatacenterBroker broker = new DatacenterBroker("Broker");

        Vm vm = new Vm(0, broker.getId(), 1000, 1, 512, 1000,

                10000, "Xen", new CloudletSchedulerSpaceShared());

        broker.submitVmList(Arrays.asList(vm));



        // Cloudlets (FCFS order)

        UtilizationModel m = new UtilizationModelFull();

        Cloudlet c1 = new Cloudlet(0, 40000, 1, 300, 300, m, m, m);

        Cloudlet c2 = new Cloudlet(1, 10000, 1, 300, 300, m, m, m);

        c1.setUserId(broker.getId());

        c2.setUserId(broker.getId());



        broker.submitCloudletList(Arrays.asList(c1, c2));



        // Run

        CloudSim.startSimulation();

        List<Cloudlet> list = broker.getCloudletReceivedList();

        CloudSim.stopSimulation();



        // Output

        System.out.println("Cloudlet ID | Status | Finish Time");

        for (Cloudlet c : list)

            System.out.println(c.getCloudletId() + " | SUCCESS | " + c.getFinishTime());

    }

}

