# java programs running scripts for hw2
iter=10000000
threadMax=16
testIter=1
KeyBits=20
echo "CoinFlips Experiment Started" > ./hw2_result.txt

echo "CoinFlips Experiment Speedup Test:" >> ./hw2_result.txt
for (( i = 1 ; i <= $threadMax ; i++))
do
	echo "" >> ./hw2_result.txt
	echo "java CoinFlip $i $iter"
	echo "java CoinFlip $i $iter" >> ./hw2_result.txt
	for (( k = 1 ; k <= $testIter ; k++ ))
	do
		java CoinFlip $i $iter >> ./hw2_result.txt
	done
done
echo "CoinFlips Experiment Speedup Test Finished" >> ./hw2_result.txt

echo "" >> ./hw2_result.txt

echo "CoinFlips Experiment Scaleup Test:" >> ./hw2_result.txt
for (( i = 1 ; i <= $threadMax ; i++))
do
	this_iter=`expr $i \* $iter`
	echo "" >> ./hw2_result.txt
	echo "java CoinFlip $i $this_iter"
	echo "java CoinFlip $i $this_iter" >> ./hw2_result.txt
	for (( k = 1 ; k <= $testIter ; k++ ))
	do
		java CoinFlip $i $this_iter >> ./hw2_result.txt
	done
done
echo "CoinFlips Experiment Scaleup Test Finished" >> ./hw2_result.txt

echo "" >> ./hw2_result.txt

echo "BruteForceDES Experiment Speedup Test:" >> ./hw2_result.txt
for i in 1 2 4 8 16
do
	echo "" >> ./hw2_result.txt
	echo "java BruteForceDES $i $KeyBits"
	echo "java BruteForceDES $i $KeyBits" >> ./hw2_result.txt
	for (( k = 1 ; k <= $testIter ; k++ ))
	do
		java BruteForceDES $i $KeyBits >> ./hw2_result.txt
	done
done
echo "BruteForceDES Experiment Speedup Test Finished" >> ./hw2_result.txt

echo "" >> ./hw2_result.txt

thisKeyBits=`expr $KeyBits - 1`
echo "BruteForceDES Experiment Scaleup Test:" >> ./hw2_result.txt
for i in 1 2 4 8 16
do
	thisKeyBits=`expr $thisKeyBits + 1`
	echo "" >> ./hw2_result.txt
	echo "java BruteForceDES $i $thisKeyBits"
	echo "java BruteForceDES $i $thisKeyBits" >> ./hw2_result.txt
	for (( k = 1 ; k <= $testIter ; k++ ))
	do
		java BruteForceDES $i $thisKeyBits >> ./hw2_result.txt
	done
done
echo "BruteForceDES Experiment Scaleup Test Finished" >> ./hw2_result.txt

echo "" >> ./hw2_result.txt



