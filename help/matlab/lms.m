% Load LMS values and plot them.
%
% $Id: lms.m,v 1.2 2003/06/06 08:43:54 tobi Exp $

load lms_values.dat
lambda = lms_values (:,1);
L = lms_values (:,2);
M = lms_values (:,3);
S = lms_values (:,4);

%set up figures for good image in help system
% set figure printing to wysiwig
set(0,'defaultfigureposition',[200,200,320,240],'defaultfigurePaperPositionMode','auto'); 
% set nice axes properties
set(0,'defaultAxesPosition',[.15,.2,.75,.65],'defaultaxesxlim',[400,700],'defaultaxeslinewidth',2,...
    'defaultaxesfontsize',11,'defaultaxesfontname','helvetica');

% plot each curve
figure(1);
plot (lambda, L, 'r', lambda, M, 'g', lambda, S, 'b','linewidth',3)
title 'Cone sensitivities (normalized)'
xlabel 'Wavelength \lambda (nm)'
ylabel 'Relative spectral sensitivity'
grid on
legend ('L', 'M', 'S')
print(gcf,'-djpeg100','..\Images\lms'); 

% plot the difference between each curve and the average of all 3
AVG=(L+M)/2;
LD=L-AVG;
MD=M-AVG;
SD=S-AVG;

figure(2);
plot (lambda, LD, 'r', lambda, MD, 'g', lambda, SD, 'b','linewidth',3)
title 'Cone - (L+M)/2'
xlabel 'Wavelength \lambda (nm)'
ylabel 'Difference'
grid on
legend ('LD', 'MD', 'SD')
print(gcf,'-djpeg100','..\Images\lms-diff'); 

% now ratios between each cone and the other 2
% LM=(L+M)/2;
% LR=L./((LM));
% MR=M./((LM));
% SR=S./((LM));
% 
% figure(3);
% plot (lambda, LR, 'r', lambda, MR, 'g', lambda, SR, 'b','linewidth',3)
% title 'Cone/(L+M+S)'
% xlabel 'Wavelength \lambda (nm)'
% ylabel 'Ratio'
% grid on
% legend ('LR', 'MR', 'SR')
% print(gcf,'-djpeg100','lms-diff'); 

% $Log: lms.m,v $
% Revision 1.2  2003/06/06 08:43:54  tobi
% modified to plot figures for help system with correct size, font, etc.
% these figures now written directly to images directory in help folder.
%