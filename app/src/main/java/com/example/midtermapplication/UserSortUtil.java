package com.example.midtermapplication;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserSortUtil {

    public static void sortUserByNameAsc(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                String lastName1 = extractLastName(user1.getName());
                String lastName2 = extractLastName(user2.getName());
                return lastName1.compareToIgnoreCase(lastName2);
            }
        });
    }

    public static void sortUserByNameDesc(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                String lastName1 = extractLastName(user1.getName());
                String lastName2 = extractLastName(user2.getName());
                return lastName2.compareToIgnoreCase(lastName1);
            }
        });

    }
    public static void sortStudentByNameAsc(List<Student> users) {
        Collections.sort(users, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                String lastName1 = extractLastName(student1.getStudentName());
                String lastName2 = extractLastName(student2.getStudentName());
                return lastName1.compareToIgnoreCase(lastName2);
            }
        });
    }

    public static void sortStudentByNameDesc(List<Student> users) {
        Collections.sort(users, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                String lastName1 = extractLastName(student1.getStudentName());
                String lastName2 = extractLastName(student2.getStudentName());
                return lastName2.compareToIgnoreCase(lastName1);
            }
        });
    }

    public static void sortStudentByEmailAsc(List<Student> users) {
        Collections.sort(users, new Comparator<Student>() {
            @Override
            public int compare(Student student1, Student student2) {
                String lastName1 = extractLastName(student1.getStudentName());
                String lastName2 = extractLastName(student2.getStudentName());
                return lastName2.compareToIgnoreCase(lastName1);
            }
        });
    }

    public static void sortStudentByEmailDesc(List<Student> users) {
        Collections.sort(users, new Comparator<Student>() {
            @Override
            public int compare(Student user1, Student user2) {
                return user2.getStudentEmail().compareToIgnoreCase(user1.getStudentEmail());
            }
        });
    }

    private static String extractLastName(String fullName) {
        String[] names = fullName.split("\\s+");
        if (names.length > 0) {
            return names[names.length - 1];
        } else {
            return fullName;
        }
    }

    public static void sortByRoleAsc(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getRole().compareToIgnoreCase(user2.getRole());
            }
        });
    }

    public static void sortByRoleDesc(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user2.getRole().compareToIgnoreCase(user1.getRole());
            }
        });
    }
    public static void sortCertificatesByNameAsc(List<Certificates> certificates) {
        Collections.sort(certificates, new Comparator<Certificates>() {
            @Override
            public int compare(Certificates cert1, Certificates cert2) {
                return cert1.getName().compareToIgnoreCase(cert2.getName());
            }
        });
    }

    public static void sortCertificatesByEmailAsc(List<Certificates> certificates) {
        Collections.sort(certificates, new Comparator<Certificates>() {
            @Override
            public int compare(Certificates cert1, Certificates cert2) {
                return cert1.getBody().compareToIgnoreCase(cert2.getBody());
            }
        });
    }

    public static void sortCertificatesByEmailDesc(List<Certificates> certificates) {
        Collections.sort(certificates, new Comparator<Certificates>() {
            @Override
            public int compare(Certificates cert1, Certificates cert2) {
                return cert2.getBody().compareToIgnoreCase(cert1.getBody());
            }
        });
    }

    public static void sortCertificatesByNameDesc(List<Certificates> certificates) {
        Collections.sort(certificates, new Comparator<Certificates>() {
            @Override
            public int compare(Certificates cert1, Certificates cert2) {
                return cert2.getName().compareToIgnoreCase(cert1.getName());
            }
        });
    }

}
