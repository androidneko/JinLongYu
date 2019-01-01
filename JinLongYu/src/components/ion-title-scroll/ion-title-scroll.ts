import { Component, Input, Output, EventEmitter } from '@angular/core';

/**
 * Generated class for the IonTitleScrollComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'ion-title-scroll',
  templateUrl: 'ion-title-scroll.html'
})
export class IonTitleScrollComponent {

  text: string;
  @Input()   selectedIndex=0;
  @Input() titleArray = [];
  @Output() itemValueChange: EventEmitter<any> = new EventEmitter();
  constructor() {
    console.log('Hello IonTitleScrollComponent Component');
    this.text = 'Hello World';
  }

  itemClicked(idx){
      this.selectedIndex = idx;
      this.itemValueChange.emit(idx);
  }

}
