package se.nackademin;

import se.nackademin.exceptions.SeveralNameExistException;
import se.nackademin.model.Member;
import se.nackademin.model.MembershipType;
import se.nackademin.service.ObjectReaderService;
import se.nackademin.service.ObjectWriterService;
import se.nackademin.service.ReadTextFileService;
import se.nackademin.utils.ListsUtils;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class ApplicationManager {

    private static final String CUSTOMERS_FILE_PATH = "src/main/resources/customers.txt";
    private static final String COACH_FILE_PATH = "src/main/resources/coach.ser";
    private static final List<Member> members = ReadTextFileService.convertCustomerFileToMemberList(Path.of(CUSTOMERS_FILE_PATH));

    public void runApplication() throws IOException {

        FileOutputStream fos = new FileOutputStream(COACH_FILE_PATH);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        System.out.println("Please click on cancel if you want to Exit!");
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Input name/personal number:");
            if (input == null) {
                System.out.println("The coach file content is:");
                for (Member m: ObjectReaderService.convertSerFileToList(COACH_FILE_PATH)) {
                    System.out.println(m);
                }
                fos.close();
                oos.close();
                break;
            }
            try {
                MembershipType type = ListsUtils.getMembershipType(members, input);
                System.out.println("Hi, " + input + " Your registration is: " + type);
                if (MembershipType.ACTIVE.equals(type)) {
                    Member member = ListsUtils.getMember(members, input);
                    member.setRegistrationDate(LocalDate.now());
                    ObjectWriterService.addNewMember(oos, member);
                }
            } catch (SeveralNameExistException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}