package pers.gym.copy;

/**
 * <p>浅拷贝
 * 浅拷贝会在堆上创建一个新的对象（区别于引用拷贝的一点），不过，如果原对象内部的属性是引用类型的话，浅拷贝会直接复制内部对象的引用地址，也就是说拷贝对象和原对象公用同一个内部对象。
 *
 * @author gym on 2023-03-28 15:23
 */
public class ShallowCopyDemo {

    public static void main(String[] args) {
        Person person = new Person(new Address("广州"));
        Person personCopy = person.clone();
        // true
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
        protected Address clone() {
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
        protected Person clone() {
            try {
                return (Person) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }
}