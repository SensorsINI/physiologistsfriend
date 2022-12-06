% plots bipolar xfer function

x=linspace(-3,3,100);
y=(tanh(x-1)+1)/2;
plot(x,y);
grid on;
xlabel input
ylabel output
