import { PipesModule } from './../pipes/pipes.module';
import { IonicModule } from 'ionic-angular';
import { NgModule } from '@angular/core';
import { IonTyfunctionGridComponent } from './ion-tyfunction-grid/ion-tyfunction-grid';
import { IonAllitemShowedBottomComponent } from './ion-allitem-showed-bottom/ion-allitem-showed-bottom';
import { IonCustomEmptyComponent } from './ion-custom-empty/ion-custom-empty';
import { JlDatePickerComponent } from './jl-date-picker/jl-date-picker';
import { JlStartendDatepickerItemComponent } from './jl-startend-datepicker-item/jl-startend-datepicker-item';
import { ImgLazyLoadComponent } from './img-lazy-load/img-lazy-load';
import { IonTitleScrollComponent } from './ion-title-scroll/ion-title-scroll';
@NgModule({
	declarations: [IonTyfunctionGridComponent,
    IonAllitemShowedBottomComponent,
    IonCustomEmptyComponent,
    JlDatePickerComponent,
    JlStartendDatepickerItemComponent,
    JlStartendDatepickerItemComponent,
    ImgLazyLoadComponent,
    IonTitleScrollComponent,
    ],

	imports: [IonicModule,PipesModule],
	exports: [IonTyfunctionGridComponent,
    IonAllitemShowedBottomComponent,
    IonCustomEmptyComponent,
    JlDatePickerComponent,
    JlStartendDatepickerItemComponent,
    JlStartendDatepickerItemComponent,
    ImgLazyLoadComponent,
    IonTitleScrollComponent,
    ]

})
export class ComponentsModule {}
