all: architecture.pdf
	
architecture.pdf: architecture.tex logical-view.tex bibliography.bib
	pdflatex architecture
	bibtex architecture
	pdflatex architecture
	pdflatex architecture

.PHONY: all clean

clean:
	rm -f *.aux
	rm -f architecture.bbl
	rm -f architecture.blg
	rm -f architecture.dvi
	rm -f architecture.log
	rm -f architecture.pdf
	rm -f architecture.toc
