package com.mycompany.manager;

import com.mycompany.entity.Member;
import com.mycompany.entity.PremiumMember;
import com.mycompany.entity.RegularMember;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for Member operations in the library management system.
 */
public class MemberManager extends BaseManager<Member> {

    public MemberManager() {
        super();
    }

    public Member getMemberById(String id) {
        for (Member m : this.items) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public String addMember(String id, String name, String phone, String email, String memberType) {
        if (id.isEmpty()) return "Fail: Member ID cannot be empty.";
        if (this.getMemberById(id) != null) return "Fail: Member ID already exists.";
        if (name.isEmpty()) return "Fail: Name cannot be empty.";

        if (memberType.equalsIgnoreCase("premium")) {
            this.items.add(new PremiumMember(id, name, phone, email));
        } else {
            this.items.add(new RegularMember(id, name, phone, email));
        }

        return "Success";
    }

    public String updateMember(String id, String newPhone, String newEmail) {
        Member m = this.getMemberById(id);
        if (m == null) return "Fail: Member not found.";

        if (!newPhone.isEmpty()) m.setPhone(newPhone);
        if (!newEmail.isEmpty()) m.setEmail(newEmail);

        return "Success";
    }

    public String removeMember(String id) {
        Member m = this.getMemberById(id);
        if (m == null) return "Fail: Member not found.";
        if (m.getCurrentBorrowCount() > 0) return "Fail: Cannot remove member with currently borrowed books.";

        this.items.remove(m);
        return "Success";
    }

    public List<Member> getAllMembers() {
        return this.getAll();
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> result = new ArrayList<>();
        for (Member m : this.items) {
            if (m.getName().toLowerCase().contains(keyword) || m.getId().toLowerCase().contains(keyword)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Member> getMembersBorrowingCount() {
        List<Member> sorted = new ArrayList<>(this.items);
        sorted.sort((m1, m2) -> Integer.compare(m2.getTotalBorrowCount(), m1.getTotalBorrowCount()));
        return sorted;
    }

    public void addLoadedMember(Member m) {
        this.addLoadedItem(m);
    }
}
