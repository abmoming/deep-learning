package pers.gym.copy;

/**
 * <p>深拷贝
 * 深拷贝会完全复制整个对象，包括这个对象所包含的内部对象。
 *
 * @author gym on 2023-03-28 16:09
 */
public class DeepCopyDemo {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person(new Address("广州"));
        Person personCopy = person.clone();
        // false
        System.out.println(person.getAddress() == personCopy.getAddress());
    }

    static class Address implements Cloneable {

        private String name;

        public Address(){}

        public Address(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        protected Address clone() throws CloneNotSupportedException {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class Person implements Cloneable {

        private Address address;

        public Person() {}

        public Person(Address address) {
            this.address = address;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        @Override
        protected Person clone() throws CloneNotSupportedException {
            try {
                Person person = (Person) super.clone();
                person.setAddress(person.getAddress().clone());
                return person;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }
}