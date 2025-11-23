#!/bin/bash

TPCC=bin/tpcc
TESTDIR=test

if [ ! -f "$TPCC" ]; then
    echo "Erreur : exécutable $TPCC non trouvé. Compile d'abord avec 'make'."
    exit 1
fi

OK=0
FAIL=0

for category in good sem-err syn-err warn; do
    echo "=== Tests $category ==="
    for testfile in $TESTDIR/$category/*.txt; do
        echo -n "Test $testfile... "

        # Exécute ton analyseur
        $TPCC < "$testfile" > output.txt 2>&1

        case $category in
            good)
                if grep -q "Erreur" output.txt || grep -q "warning" output.txt; then
                    echo "❌ (erreurs ou warnings trouvés)"
                    FAIL=$((FAIL+1))
                else
                    echo "✅"
                    OK=$((OK+1))
                fi
                ;;
            syn-err|sem-err)
                if grep -q "Erreur" output.txt; then
                    echo "✅ (erreur détectée)"
                    OK=$((OK+1))
                else
                    echo "❌ (pas d'erreur détectée)"
                    FAIL=$((FAIL+1))
                fi
                ;;
            warn)
                if grep -q "Warning" output.txt; then
                    echo "✅ (warning détecté)"
                    OK=$((OK+1))
                else
                    echo "❌ (pas de warning détecté)"
                    FAIL=$((FAIL+1))
                fi
                ;;
        esac
    done
done

echo "================================"
echo " Résultats : $OK OK / $((OK+FAIL)) tests"
echo "================================"

rm -f output.txt
