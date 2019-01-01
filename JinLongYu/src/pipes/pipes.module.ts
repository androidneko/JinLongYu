import { NgModule } from '@angular/core';
import { TydatePipe } from './tydate/tydate';
import { ParseFloatPipe } from './parse-float/parse-float';
import { FloatSpliterPipe } from './float-spliter/float-spliter';
import { ParseAmountPipe } from './parse-amount/parse-amount';
import { OutStoragePipe } from './out-storage/out-storage';
import { HtmlPipe } from './html/html';
import { TextDatePipe } from './text-date/text-date';
import { StateTipPipe } from './state-tip/state-tip';
import { ConsumeTipPipe } from './consume-tip/consume-tip';
import { DateFormatPipe } from './date-format/date-format';
@NgModule({
	declarations: [
    TydatePipe,
    ParseFloatPipe,
    FloatSpliterPipe,
    ParseAmountPipe,
    OutStoragePipe,
    HtmlPipe,
    TextDatePipe,
    StateTipPipe,
    ConsumeTipPipe,
    DateFormatPipe],
	imports: [],
	exports: [
    TydatePipe,
    ParseFloatPipe,
    FloatSpliterPipe,
    ParseAmountPipe,
    OutStoragePipe,
    HtmlPipe,
    TextDatePipe,
    StateTipPipe,
    ConsumeTipPipe,
    DateFormatPipe]
})
export class PipesModule {}
