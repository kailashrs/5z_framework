package com.android.internal.telephony;

import android.telephony.Rlog;
import java.util.Arrays;

public final class RoamAliasAsus
{
  static final String LOG_TAG = "RoamAliasAsus";
  static final RoamAliasItem[][] RoamAliasTab;
  
  static
  {
    RoamAliasItem localRoamAliasItem1 = new RoamAliasItem(202, 9);
    RoamAliasItem localRoamAliasItem2 = new RoamAliasItem(202, 10);
    RoamAliasItem localRoamAliasItem3 = new RoamAliasItem(208, 13);
    RoamAliasItem localRoamAliasItem4 = new RoamAliasItem(208, 88);
    RoamAliasItem localRoamAliasItem5 = new RoamAliasItem(208, 9);
    RoamAliasItem localRoamAliasItem6 = new RoamAliasItem(208, 10);
    RoamAliasItem localRoamAliasItem7 = new RoamAliasItem(232, 5);
    RoamAliasItem localRoamAliasItem8 = new RoamAliasItem(232, 10);
    RoamAliasItem localRoamAliasItem9 = new RoamAliasItem(234, 0);
    RoamAliasItem localRoamAliasItem10 = new RoamAliasItem(234, 77);
    RoamAliasItem localRoamAliasItem11 = new RoamAliasItem(234, 30);
    RoamAliasItem localRoamAliasItem12 = new RoamAliasItem(234, 31);
    RoamAliasItem localRoamAliasItem13 = new RoamAliasItem(234, 32);
    RoamAliasItem localRoamAliasItem14 = new RoamAliasItem(234, 33);
    RoamAliasItem localRoamAliasItem15 = new RoamAliasItem(234, 34);
    RoamAliasItem localRoamAliasItem16 = new RoamAliasItem(234, 86);
    RoamAliasItem localRoamAliasItem17 = new RoamAliasItem(234, 9);
    RoamAliasItem localRoamAliasItem18 = new RoamAliasItem(234, 19);
    RoamAliasItem localRoamAliasItem19 = new RoamAliasItem(234, 10);
    RoamAliasItem localRoamAliasItem20 = new RoamAliasItem(234, 11);
    RoamAliasItem localRoamAliasItem21 = new RoamAliasItem(238, 2);
    RoamAliasItem localRoamAliasItem22 = new RoamAliasItem(238, 77);
    RoamAliasItem localRoamAliasItem23 = new RoamAliasItem(240, 8);
    RoamAliasItem localRoamAliasItem24 = new RoamAliasItem(240, 24);
    RoamAliasItem localRoamAliasItem25 = new RoamAliasItem(242, 4);
    RoamAliasItem localRoamAliasItem26 = new RoamAliasItem(242, 5);
    RoamAliasItem localRoamAliasItem27 = new RoamAliasItem(244, 3);
    RoamAliasItem localRoamAliasItem28 = new RoamAliasItem(244, 12);
    RoamAliasItem localRoamAliasItem29 = new RoamAliasItem(244, 5);
    RoamAliasItem localRoamAliasItem30 = new RoamAliasItem(244, 21);
    RoamAliasItem localRoamAliasItem31 = new RoamAliasItem(250, 3);
    RoamAliasItem localRoamAliasItem32 = new RoamAliasItem(250, 5);
    RoamAliasItem localRoamAliasItem33 = new RoamAliasItem(250, 12);
    RoamAliasItem localRoamAliasItem34 = new RoamAliasItem(250, 17);
    RoamAliasItem localRoamAliasItem35 = new RoamAliasItem(250, 38);
    RoamAliasItem localRoamAliasItem36 = new RoamAliasItem(250, 39);
    RoamAliasItem localRoamAliasItem37 = new RoamAliasItem(262, 3);
    RoamAliasItem localRoamAliasItem38 = new RoamAliasItem(262, 5);
    RoamAliasItem localRoamAliasItem39 = new RoamAliasItem(262, 77);
    RoamAliasItem localRoamAliasItem40 = new RoamAliasItem(262, 7);
    RoamAliasItem localRoamAliasItem41 = new RoamAliasItem(262, 8);
    RoamAliasItem localRoamAliasItem42 = new RoamAliasItem(262, 11);
    RoamAliasItem localRoamAliasItem43 = new RoamAliasItem(262, 2);
    RoamAliasItem localRoamAliasItem44 = new RoamAliasItem(262, 4);
    RoamAliasItem localRoamAliasItem45 = new RoamAliasItem(262, 9);
    RoamAliasItem localRoamAliasItem46 = new RoamAliasItem(262, 1);
    RoamAliasItem localRoamAliasItem47 = new RoamAliasItem(262, 6);
    RoamAliasItem localRoamAliasItem48 = new RoamAliasItem(274, 1);
    RoamAliasItem localRoamAliasItem49 = new RoamAliasItem(274, 12);
    RoamAliasItem localRoamAliasItem50 = new RoamAliasItem(302, 86);
    RoamAliasItem localRoamAliasItem51 = new RoamAliasItem(302, 220);
    RoamAliasItem localRoamAliasItem52 = new RoamAliasItem(302, 860);
    RoamAliasItem localRoamAliasItem53 = new RoamAliasItem(302, 68);
    RoamAliasItem localRoamAliasItem54 = new RoamAliasItem(302, 680);
    RoamAliasItem localRoamAliasItem55 = new RoamAliasItem(302, 64);
    RoamAliasItem localRoamAliasItem56 = new RoamAliasItem(302, 610);
    RoamAliasItem localRoamAliasItem57 = new RoamAliasItem(302, 640);
    RoamAliasItem localRoamAliasItem58 = new RoamAliasItem(310, 30);
    RoamAliasItem localRoamAliasItem59 = new RoamAliasItem(310, 38);
    RoamAliasItem localRoamAliasItem60 = new RoamAliasItem(310, 70);
    RoamAliasItem localRoamAliasItem61 = new RoamAliasItem(310, 90);
    RoamAliasItem localRoamAliasItem62 = new RoamAliasItem(310, 150);
    RoamAliasItem localRoamAliasItem63 = new RoamAliasItem(310, 170);
    RoamAliasItem localRoamAliasItem64 = new RoamAliasItem(310, 280);
    RoamAliasItem localRoamAliasItem65 = new RoamAliasItem(310, 380);
    RoamAliasItem localRoamAliasItem66 = new RoamAliasItem(310, 410);
    RoamAliasItem localRoamAliasItem67 = new RoamAliasItem(310, 560);
    RoamAliasItem localRoamAliasItem68 = new RoamAliasItem(310, 680);
    RoamAliasItem localRoamAliasItem69 = new RoamAliasItem(310, 980);
    RoamAliasItem localRoamAliasItem70 = new RoamAliasItem(310, 590);
    RoamAliasItem localRoamAliasItem71 = new RoamAliasItem(310, 890);
    RoamAliasItem localRoamAliasItem72 = new RoamAliasItem(310, 530);
    RoamAliasItem localRoamAliasItem73 = new RoamAliasItem(310, 770);
    RoamAliasItem localRoamAliasItem74 = new RoamAliasItem(310, 26);
    RoamAliasItem localRoamAliasItem75 = new RoamAliasItem(310, 31);
    RoamAliasItem localRoamAliasItem76 = new RoamAliasItem(310, 160);
    RoamAliasItem localRoamAliasItem77 = new RoamAliasItem(310, 200);
    RoamAliasItem localRoamAliasItem78 = new RoamAliasItem(310, 210);
    RoamAliasItem localRoamAliasItem79 = new RoamAliasItem(310, 240);
    RoamAliasItem localRoamAliasItem80 = new RoamAliasItem(310, 250);
    RoamAliasItem localRoamAliasItem81 = new RoamAliasItem(310, 260);
    RoamAliasItem localRoamAliasItem82 = new RoamAliasItem(310, 270);
    RoamAliasItem localRoamAliasItem83 = new RoamAliasItem(310, 310);
    RoamAliasItem localRoamAliasItem84 = new RoamAliasItem(310, 490);
    RoamAliasItem localRoamAliasItem85 = new RoamAliasItem(310, 40);
    RoamAliasItem localRoamAliasItem86 = new RoamAliasItem(310, 330);
    RoamAliasItem localRoamAliasItem87 = new RoamAliasItem(310, 0);
    RoamAliasItem localRoamAliasItem88 = new RoamAliasItem(310, 2);
    RoamAliasItem localRoamAliasItem89 = new RoamAliasItem(310, 9);
    RoamAliasItem localRoamAliasItem90 = new RoamAliasItem(310, 290);
    RoamAliasItem localRoamAliasItem91 = new RoamAliasItem(311, 73);
    RoamAliasItem localRoamAliasItem92 = new RoamAliasItem(311, 540);
    RoamAliasItem localRoamAliasItem93 = new RoamAliasItem(311, 730);
    RoamAliasItem localRoamAliasItem94 = new RoamAliasItem(311, 1);
    RoamAliasItem localRoamAliasItem95 = new RoamAliasItem(311, 5);
    RoamAliasItem localRoamAliasItem96 = new RoamAliasItem(311, 150);
    RoamAliasItem localRoamAliasItem97 = new RoamAliasItem(376, 352);
    RoamAliasItem localRoamAliasItem98 = new RoamAliasItem(376, 360);
    RoamAliasItem localRoamAliasItem99 = new RoamAliasItem(418, 0);
    RoamAliasItem localRoamAliasItem100 = new RoamAliasItem(418, 5);
    RoamAliasItem localRoamAliasItem101 = new RoamAliasItem(436, 1);
    RoamAliasItem localRoamAliasItem102 = new RoamAliasItem(436, 2);
    RoamAliasItem localRoamAliasItem103 = new RoamAliasItem(450, 2);
    RoamAliasItem localRoamAliasItem104 = new RoamAliasItem(450, 8);
    RoamAliasItem localRoamAliasItem105 = new RoamAliasItem(454, 16);
    RoamAliasItem localRoamAliasItem106 = new RoamAliasItem(454, 19);
    RoamAliasItem localRoamAliasItem107 = new RoamAliasItem(454, 20);
    RoamAliasItem localRoamAliasItem108 = new RoamAliasItem(454, 0);
    RoamAliasItem localRoamAliasItem109 = new RoamAliasItem(454, 1);
    RoamAliasItem localRoamAliasItem110 = new RoamAliasItem(454, 2);
    RoamAliasItem localRoamAliasItem111 = new RoamAliasItem(454, 10);
    RoamAliasItem localRoamAliasItem112 = new RoamAliasItem(454, 18);
    RoamAliasItem localRoamAliasItem113 = new RoamAliasItem(454, 6);
    RoamAliasItem localRoamAliasItem114 = new RoamAliasItem(454, 15);
    RoamAliasItem localRoamAliasItem115 = new RoamAliasItem(454, 17);
    RoamAliasItem localRoamAliasItem116 = new RoamAliasItem(454, 12);
    RoamAliasItem localRoamAliasItem117 = new RoamAliasItem(454, 13);
    RoamAliasItem localRoamAliasItem118 = new RoamAliasItem(454, 3);
    RoamAliasItem localRoamAliasItem119 = new RoamAliasItem(454, 4);
    RoamAliasItem localRoamAliasItem120 = new RoamAliasItem(455, 3);
    RoamAliasItem localRoamAliasItem121 = new RoamAliasItem(455, 5);
    RoamAliasItem localRoamAliasItem122 = new RoamAliasItem(455, 1);
    RoamAliasItem localRoamAliasItem123 = new RoamAliasItem(455, 4);
    RoamAliasItem localRoamAliasItem124 = new RoamAliasItem(456, 5);
    RoamAliasItem localRoamAliasItem125 = new RoamAliasItem(456, 6);
    RoamAliasItem localRoamAliasItem126 = new RoamAliasItem(460, 0);
    RoamAliasItem localRoamAliasItem127 = new RoamAliasItem(460, 2);
    RoamAliasItem localRoamAliasItem128 = new RoamAliasItem(460, 7);
    RoamAliasItem[] arrayOfRoamAliasItem1 = { new RoamAliasItem(460, 1), new RoamAliasItem(460, 6), new RoamAliasItem(460, 9) };
    RoamAliasItem localRoamAliasItem129 = new RoamAliasItem(466, 1);
    RoamAliasItem localRoamAliasItem130 = new RoamAliasItem(466, 88);
    RoamAliasItem localRoamAliasItem131 = new RoamAliasItem(466, 5);
    RoamAliasItem localRoamAliasItem132 = new RoamAliasItem(466, 97);
    RoamAliasItem localRoamAliasItem133 = new RoamAliasItem(466, 93);
    RoamAliasItem localRoamAliasItem134 = new RoamAliasItem(466, 97);
    RoamAliasItem localRoamAliasItem135 = new RoamAliasItem(466, 99);
    RoamAliasItem localRoamAliasItem136 = new RoamAliasItem(502, 0);
    RoamAliasItem localRoamAliasItem137 = new RoamAliasItem(502, 1);
    RoamAliasItem localRoamAliasItem138 = new RoamAliasItem(510, 1);
    RoamAliasItem localRoamAliasItem139 = new RoamAliasItem(510, 21);
    RoamAliasItem[] arrayOfRoamAliasItem2 = { new RoamAliasItem(520, 0), new RoamAliasItem(520, 4), new RoamAliasItem(520, 99) };
    RoamAliasItem localRoamAliasItem140 = new RoamAliasItem(520, 1);
    RoamAliasItem localRoamAliasItem141 = new RoamAliasItem(520, 3);
    RoamAliasItem localRoamAliasItem142 = new RoamAliasItem(520, 5);
    RoamAliasItem localRoamAliasItem143 = new RoamAliasItem(520, 18);
    RoamAliasItem localRoamAliasItem144 = new RoamAliasItem(525, 1);
    RoamAliasItem localRoamAliasItem145 = new RoamAliasItem(525, 2);
    RoamAliasItem[] arrayOfRoamAliasItem3 = { new RoamAliasItem(606, 91), new RoamAliasItem(606, 218) };
    RoamAliasItem localRoamAliasItem146 = new RoamAliasItem(629, 1);
    RoamAliasItem localRoamAliasItem147 = new RoamAliasItem(629, 7);
    RoamAliasItem localRoamAliasItem148 = new RoamAliasItem(635, 1);
    RoamAliasItem localRoamAliasItem149 = new RoamAliasItem(635, 4);
    RoamAliasItem localRoamAliasItem150 = new RoamAliasItem(641, 1);
    RoamAliasItem localRoamAliasItem151 = new RoamAliasItem(641, 22);
    RoamAliasItem localRoamAliasItem152 = new RoamAliasItem(710, 21);
    RoamAliasItem localRoamAliasItem153 = new RoamAliasItem(710, 73);
    RoamAliasItem localRoamAliasItem154 = new RoamAliasItem(712, 0);
    RoamAliasItem localRoamAliasItem155 = new RoamAliasItem(712, 1);
    RoamAliasItem localRoamAliasItem156 = new RoamAliasItem(712, 2);
    RoamAliasItem localRoamAliasItem157 = new RoamAliasItem(722, 36);
    RoamAliasItem localRoamAliasItem158 = new RoamAliasItem(722, 341);
    RoamAliasItem localRoamAliasItem159 = new RoamAliasItem(724, 32);
    RoamAliasItem localRoamAliasItem160 = new RoamAliasItem(724, 33);
    RoamAliasItem localRoamAliasItem161 = new RoamAliasItem(724, 34);
    RoamAliasItem localRoamAliasItem162 = new RoamAliasItem(724, 16);
    RoamAliasItem localRoamAliasItem163 = new RoamAliasItem(724, 24);
    RoamAliasItem localRoamAliasItem164 = new RoamAliasItem(724, 31);
    RoamAliasItem localRoamAliasItem165 = new RoamAliasItem(724, 6);
    RoamAliasItem localRoamAliasItem166 = new RoamAliasItem(724, 10);
    RoamAliasItem localRoamAliasItem167 = new RoamAliasItem(724, 11);
    RoamAliasItem localRoamAliasItem168 = new RoamAliasItem(724, 23);
    RoamAliasItem localRoamAliasItem169 = new RoamAliasItem(724, 2);
    RoamAliasItem localRoamAliasItem170 = new RoamAliasItem(724, 3);
    RoamAliasItem localRoamAliasItem171 = new RoamAliasItem(724, 4);
    RoamAliasItem localRoamAliasItem172 = new RoamAliasItem(730, 1);
    RoamAliasItem localRoamAliasItem173 = new RoamAliasItem(730, 10);
    RoamAliasItem localRoamAliasItem174 = new RoamAliasItem(730, 2);
    RoamAliasItem localRoamAliasItem175 = new RoamAliasItem(730, 7);
    RoamAliasItem localRoamAliasItem176 = new RoamAliasItem(732, 103);
    RoamAliasItem localRoamAliasItem177 = new RoamAliasItem(732, 111);
    RoamAliasTab = new RoamAliasItem[][] { { localRoamAliasItem1, localRoamAliasItem2 }, { localRoamAliasItem3, localRoamAliasItem4 }, { localRoamAliasItem5, localRoamAliasItem6 }, { localRoamAliasItem7, localRoamAliasItem8 }, { localRoamAliasItem9, localRoamAliasItem10 }, { localRoamAliasItem11, localRoamAliasItem12, localRoamAliasItem13, localRoamAliasItem14, localRoamAliasItem15, localRoamAliasItem16 }, { localRoamAliasItem17, localRoamAliasItem18 }, { localRoamAliasItem19, localRoamAliasItem20 }, { localRoamAliasItem21, localRoamAliasItem22 }, { localRoamAliasItem23, localRoamAliasItem24 }, { localRoamAliasItem25, localRoamAliasItem26 }, { localRoamAliasItem27, localRoamAliasItem28 }, { localRoamAliasItem29, localRoamAliasItem30 }, { localRoamAliasItem31, localRoamAliasItem32, localRoamAliasItem33, localRoamAliasItem34, localRoamAliasItem35, localRoamAliasItem36 }, { localRoamAliasItem37, localRoamAliasItem38, localRoamAliasItem39 }, { localRoamAliasItem40, localRoamAliasItem41, localRoamAliasItem42 }, { localRoamAliasItem43, localRoamAliasItem44, localRoamAliasItem45 }, { localRoamAliasItem46, localRoamAliasItem47 }, { localRoamAliasItem48, localRoamAliasItem49 }, { localRoamAliasItem50, localRoamAliasItem51, localRoamAliasItem52 }, { localRoamAliasItem53, localRoamAliasItem54 }, { localRoamAliasItem55, localRoamAliasItem56, localRoamAliasItem57 }, { localRoamAliasItem58, localRoamAliasItem59, localRoamAliasItem60, localRoamAliasItem61, localRoamAliasItem62, localRoamAliasItem63, localRoamAliasItem64, localRoamAliasItem65, localRoamAliasItem66, localRoamAliasItem67, localRoamAliasItem68, localRoamAliasItem69 }, { localRoamAliasItem70, localRoamAliasItem71 }, { localRoamAliasItem72, localRoamAliasItem73 }, { localRoamAliasItem74, localRoamAliasItem75, localRoamAliasItem76, localRoamAliasItem77, localRoamAliasItem78, localRoamAliasItem79, localRoamAliasItem80, localRoamAliasItem81, localRoamAliasItem82, localRoamAliasItem83, localRoamAliasItem84 }, { localRoamAliasItem85, localRoamAliasItem86 }, { localRoamAliasItem87, localRoamAliasItem88, localRoamAliasItem89, localRoamAliasItem90 }, { localRoamAliasItem91, localRoamAliasItem92, localRoamAliasItem93 }, { localRoamAliasItem94, localRoamAliasItem95, localRoamAliasItem96 }, { localRoamAliasItem97, localRoamAliasItem98 }, { localRoamAliasItem99, localRoamAliasItem100 }, { localRoamAliasItem101, localRoamAliasItem102 }, { localRoamAliasItem103, localRoamAliasItem104 }, { localRoamAliasItem105, localRoamAliasItem106, localRoamAliasItem107 }, { localRoamAliasItem108, localRoamAliasItem109, localRoamAliasItem110, localRoamAliasItem111, localRoamAliasItem112 }, { localRoamAliasItem113, localRoamAliasItem114, localRoamAliasItem115 }, { localRoamAliasItem116, localRoamAliasItem117 }, { localRoamAliasItem118, localRoamAliasItem119 }, { localRoamAliasItem120, localRoamAliasItem121 }, { localRoamAliasItem122, localRoamAliasItem123 }, { localRoamAliasItem124, localRoamAliasItem125 }, { localRoamAliasItem126, localRoamAliasItem127, localRoamAliasItem128 }, arrayOfRoamAliasItem1, { localRoamAliasItem129, localRoamAliasItem130 }, { localRoamAliasItem131, localRoamAliasItem132 }, { localRoamAliasItem133, localRoamAliasItem134, localRoamAliasItem135 }, { localRoamAliasItem136, localRoamAliasItem137 }, { localRoamAliasItem138, localRoamAliasItem139 }, arrayOfRoamAliasItem2, { localRoamAliasItem140, localRoamAliasItem141 }, { localRoamAliasItem142, localRoamAliasItem143 }, { localRoamAliasItem144, localRoamAliasItem145 }, arrayOfRoamAliasItem3, { localRoamAliasItem146, localRoamAliasItem147 }, { localRoamAliasItem148, localRoamAliasItem149 }, { localRoamAliasItem150, localRoamAliasItem151 }, { localRoamAliasItem152, localRoamAliasItem153 }, { localRoamAliasItem154, localRoamAliasItem155, localRoamAliasItem156 }, { localRoamAliasItem157, localRoamAliasItem158 }, { localRoamAliasItem159, localRoamAliasItem160, localRoamAliasItem161 }, { localRoamAliasItem162, localRoamAliasItem163, localRoamAliasItem164 }, { localRoamAliasItem165, localRoamAliasItem166, localRoamAliasItem167, localRoamAliasItem168 }, { localRoamAliasItem169, localRoamAliasItem170, localRoamAliasItem171 }, { localRoamAliasItem172, localRoamAliasItem173 }, { localRoamAliasItem174, localRoamAliasItem175 }, { localRoamAliasItem176, localRoamAliasItem177 } };
  }
  
  public RoamAliasAsus() {}
  
  public static RoamAliasItem[] getMatchRoamAlias(String paramString)
  {
    if ((paramString != null) && (paramString.length() >= 5))
    {
      int i = Integer.valueOf(paramString.substring(0, 3), 10).intValue();
      int j = Integer.valueOf(paramString.substring(3, paramString.length()), 10).intValue();
      Object localObject1 = RoamAliasTab;
      int k = localObject1.length;
      for (int m = 0; m < k; m++) {
        for (Object localObject2 : localObject1[m]) {
          if (mcc == i)
          {
            if (mnc == j)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("Match alias SIM ");
              ((StringBuilder)localObject1).append(paramString);
              ((StringBuilder)localObject1).append(" in list ");
              ((StringBuilder)localObject1).append(Arrays.toString(???));
              Rlog.i("RoamAliasAsus", ((StringBuilder)localObject1).toString());
              return ???;
            }
            if (mnc > j) {
              break;
            }
          }
          else
          {
            if (mcc < i) {
              break;
            }
            if (mcc > i) {
              return new RoamAliasItem[] { new RoamAliasItem(0, 0) };
            }
          }
        }
      }
      return new RoamAliasItem[] { new RoamAliasItem(0, 0) };
    }
    return null;
  }
  
  public static boolean isSameOperatorsAsus(String paramString, RoamAliasItem[] paramArrayOfRoamAliasItem)
  {
    if ((paramArrayOfRoamAliasItem != null) && (paramString != null) && (paramString.length() >= 5))
    {
      int i = Integer.valueOf(paramString.substring(0, 3), 10).intValue();
      if (0mcc != i) {
        return false;
      }
      int j = Integer.valueOf(paramString.substring(3, paramString.length()), 10).intValue();
      int k = paramArrayOfRoamAliasItem.length;
      for (i = 0; i < k; i++)
      {
        paramString = paramArrayOfRoamAliasItem[i];
        if (mnc == j) {
          return true;
        }
        if (mnc > j) {
          break;
        }
      }
      return false;
    }
    return false;
  }
  
  public static class RoamAliasItem
  {
    private int mcc;
    private int mnc;
    
    RoamAliasItem(int paramInt1, int paramInt2)
    {
      mcc = paramInt1;
      mnc = paramInt2;
    }
    
    public String toString()
    {
      if (String.valueOf(mnc).length() < 2)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(String.valueOf(mcc));
        localStringBuilder.append("0");
        localStringBuilder.append(String.valueOf(mnc));
        return localStringBuilder.toString();
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(String.valueOf(mcc));
      localStringBuilder.append(String.valueOf(mnc));
      return localStringBuilder.toString();
    }
  }
}
