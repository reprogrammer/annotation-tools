package annotator.tests;

public class InnerReceivers {

    InnerReceivers i = new InnerReceivers() {

        void m() {}

        class Inner {
            void m() {}

            void m1(Inner this) {}
        }
    };

    void m() {}

    void m1(InnerReceivers this) {}

    void m2(annotator.tests.InnerReceivers this) {}

    class Inner1<Y, Z> {

        void m() {}

        void m1(InnerReceivers.Inner1<Y, Z> this) {}

        void m2(annotator.tests.InnerReceivers.Inner1<Y, Z> this) {}

        class Inner2 {

            void m() {}

            void m1(InnerReceivers.Inner1<Y, Z>.Inner2 this) {}
        }
    }

    static class StaticInner1 {

        void m() {}

        void m1(InnerReceivers.StaticInner1 this) {}

        void m2(annotator.tests.InnerReceivers.StaticInner1 this) {}
    }
}

class Outer<K> {
    static class StaticInner2 {

        void m() {}

        void m1(Outer.StaticInner2 this) {}

        void m2(annotator.tests.Outer.StaticInner2 this) {}

        static class StaticInner3 {

            void m() {}
        }
    }
}
