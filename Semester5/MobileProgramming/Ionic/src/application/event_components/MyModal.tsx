import React from "react";
import { createAnimation, IonModal } from "@ionic/react";

interface MyModalProps {
  isOpen: boolean;
}

export const MyModal: React.FC<MyModalProps> = (props) => {
  const enterAnimation = (baseEl: any) => {
    const backdropAnimation = createAnimation()
      .addElement(baseEl.querySelector("ion-backdrop")!)
      .fromTo("opacity", "0.01", "var(--backdrop-opacity)");

    const wrapperAnimation = createAnimation()
      .addElement(baseEl.querySelector(".modal-wrapper")!)
      .keyframes([
        { offset: 0, opacity: "0", transform: "scale(0)" },
        { offset: 1, opacity: "0.99", transform: "scale(1)" },
      ]);

    return createAnimation()
      .addElement(baseEl)
      .easing("ease-out")
      .duration(500)
      .addAnimation([backdropAnimation, wrapperAnimation]);
  };

  const leaveAnimation = (baseEl: any) => {
    return enterAnimation(baseEl).direction("reverse");
  };

  return (
    <IonModal
      isOpen={props.isOpen}
      enterAnimation={enterAnimation}
      leaveAnimation={leaveAnimation}
    >
      {props.children}
    </IonModal>
  );
};
