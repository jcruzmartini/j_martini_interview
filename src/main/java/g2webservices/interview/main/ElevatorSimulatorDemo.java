package g2webservices.interview.main;

import static g2webservices.interview.utils.TimeUtils.simulate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import g2webservices.interview.enums.StatusEnum;
import g2webservices.interview.handlers.ElevatorKeyCardRequestHandler;
import g2webservices.interview.handlers.ElevatorRequestHandler;
import g2webservices.interview.handlers.ElevatorSimpleRequestHandler;
import g2webservices.interview.keycard.DummyCardAccessSystem;
import g2webservices.interview.keycard.KeyCardAccessSystem;
import g2webservices.interview.keycard.KeyCardReader;
import g2webservices.interview.keycard.ManualUserKeyReader;
import g2webservices.interview.manager.ElevatorRequestManager;
import g2webservices.interview.manager.ElevatorRequestManagerImpl;
import g2webservices.interview.models.Building;
import g2webservices.interview.models.Floor;
import g2webservices.interview.models.elevator.Cabin;
import g2webservices.interview.models.elevator.Elevator;
import g2webservices.interview.models.elevator.ElevatorImpl;
import g2webservices.interview.models.elevator.ElevatorRequest;
import g2webservices.interview.models.elevator.ElevatorState;

/**
 * Hello world!
 *
 */
public class ElevatorSimulatorDemo {
	final private static int MAX_FLOOR = 50;
	final private static int MIN_FLOOR = -1;
	final private static int MAX_CAP_PUBLIC = 1;
	final private static int MAX_CAP_FREIGHT = 3;

	public static void main(String[] args) {
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final ExecutorService executorFreight = Executors.newSingleThreadExecutor();
		

		Building bulding = new Building(MAX_FLOOR, MIN_FLOOR);

		Cabin cabin = new Cabin("Cab Public Elevator");
		Elevator elevator = new ElevatorImpl("Public", new ElevatorState(null, 0, StatusEnum.IDLE), MAX_CAP_PUBLIC,
				Stream.of(MIN_FLOOR, MAX_FLOOR).collect(Collectors.toSet()), cabin);
		bulding.addElevator(elevator);
		setFloorsToBeNotified(elevator);

		KeyCardReader reader = new ManualUserKeyReader();
		KeyCardAccessSystem keyCard = new DummyCardAccessSystem(reader);
		ElevatorRequestHandler handler = new ElevatorKeyCardRequestHandler(elevator, keyCard);
		ElevatorRequestManager pManager = new ElevatorRequestManagerImpl(elevator, handler);
		executor.submit(pManager);


		System.out.println("Running Demo For Public Elevator");

		// Correct behavior should be:
		// Elevator moves from 0 to 5 -> Open/Close Door
		// Elevator moves from 5 to 16 -> Open/Close Door
		// Elevator moves from 16 to -1 -> Ask Key Access , if OK ->
		// Open/Close Door, Fail -> NOTHING
		// Elevator moves from -1 to 30 -> Open/Close Door
		// Elevator stop for overwheight
		ElevatorRequest upTo16 = new ElevatorRequest(16, 0);
		pManager.send(upTo16);

		simulate(2);

		ElevatorRequest downToBasement = new ElevatorRequest(-1, 0.3f);
		pManager.send(downToBasement);

		simulate(2);

		ElevatorRequest upTo5 = new ElevatorRequest(5, 0.3f);
		pManager.send(upTo5);

		simulate(2);

		ElevatorRequest upTo50 = new ElevatorRequest(30, 0.3f);
		pManager.send(upTo50);

		simulate(2);

		ElevatorRequest exceeded = new ElevatorRequest(5, 2f);
		pManager.send(exceeded);
		
		
		simulate(1000);
		System.out.println("2. Run Demo For Freight Elevator");

		Cabin cabinFreight = new Cabin("Cab Freight Elevator");
		Elevator freightElevator = new ElevatorImpl("Freight", new ElevatorState(null, 0, StatusEnum.IDLE),
				MAX_CAP_FREIGHT, null, cabinFreight);
		setFloorsToBeNotified(freightElevator);
		bulding.addElevator(freightElevator);

		ElevatorRequestHandler handlerFr = new ElevatorSimpleRequestHandler(freightElevator);
		ElevatorRequestManager fRManager = new ElevatorRequestManagerImpl(freightElevator, handlerFr);
		executorFreight.submit(fRManager);
		// Correct behavior should be :
		// Elevator moves from 0 to 5 -> Open/Close Door
		// Elevator moves from 5 to 16 -> Open/Close Door
		// Elevator moves from 16 to -1 -> Open/Close Door
		// Elevator moves from -1 to 5 -> Open/Close Door

		ElevatorRequest upTo16Bis = new ElevatorRequest(16, 0.2f);
		fRManager.send(upTo16Bis);

		simulate(2);

		ElevatorRequest downToBasementBis = new ElevatorRequest(-1, 0.2f);
		fRManager.send(downToBasementBis);

		simulate(2);

		ElevatorRequest upTo5Bis = new ElevatorRequest(5, 0);
		fRManager.send(upTo5Bis);

		simulate(2);

		ElevatorRequest at5 = new ElevatorRequest(5, 2);
		fRManager.send(at5);

	}

	private static void setFloorsToBeNotified(Elevator e) {
		for (int i = -1; i <= 50; i++) {
			e.addObserver(new Floor(String.valueOf(i), i));
		}
	}
}
