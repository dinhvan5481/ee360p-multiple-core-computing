extern crate time;
extern crate crossbeam;

use time::PreciseTime;
use std::fs::File;
use std::io::Read;

fn create_empty_matrix(rows: usize, cols: usize) -> Vec<Vec<i32>> {
    let mut matrix : Vec<Vec<i32>> = Vec::new();
    for _ in 0..rows {
        matrix.push(vec![0; cols as usize]);
    }
    matrix
}

fn load_from_file(mut file: File) -> Vec<Vec<i32>> {
    let mut matrix_text = String::new();
    file.read_to_string(&mut matrix_text).expect("Couldn't read file");

    let mut matrix_numbers = matrix_text.split_whitespace();
    let mut count = 0;
    let mut matrix : Vec<Vec<i32>>;

    let rows = matrix_numbers.next().unwrap().parse::<i32>().expect("Only numbers!") as usize;
    let cols = matrix_numbers.next().unwrap().parse::<i32>().expect("Only numbers!") as usize;

    matrix = create_empty_matrix(rows, cols);

    for number in matrix_numbers {
        let integer : i32 = number.parse().expect("There should be only numbers in the file.");
        let row = count / cols as usize;
        let col = count % cols as usize;
        matrix[row][col] = integer;
        count += 1;
    }

    matrix
}

fn multiply(matrix1 : &Vec<Vec<i32>>, matrix2 : &Vec<Vec<i32>>) -> Vec<Vec<i32>> {
    let m1_cols = matrix1[0].len();
    let m2_rows = matrix2.len();

    let m3_rows = matrix1.len();
    let m3_cols = matrix2[0].len();
	 
    let mut matrix3 = create_empty_matrix(m3_rows, m3_cols);

    if m1_cols != m2_rows {
	    print!("Matrix size do not match for multiplication");
	    return matrix3;
    }

    let start = PreciseTime::now();
    
    crossbeam::scope(|scope| {
        let mut row_index = 0;
        for row in &mut matrix3 { // We want each thread to modify `row`
            scope.spawn(move || { // Give ownership of a copy of `row_index` to each thread
                for col2 in 0..m3_cols {
                    let mut sum = 0;
                    for col1 in 0..m1_cols { // Matrix1 Columns
                        sum += matrix1[row_index][col1] * matrix2[col1][col2];
                    }
                    row[col2] = sum;
                }
            });
            row_index += 1;
        }
    });

    println!("{:?}", start.to(PreciseTime::now()));

    matrix3
}

fn print_matrix(matrix : &Vec<Vec<i32>>) {
    println!("{} {}", matrix.len(), matrix[0].len());
    for row in 0..matrix.len() {
        for col in 0..matrix[row].len() {
            print!("{} ", matrix[row][col]);
        }
        println!("");
    }
}

fn main() {
    let file1 = File::open("matrix1.in").expect("Couldnt read file1");
    let file2 = File::open("matrix2.in").expect("Couldnt read file2");

    let matrix1 = load_from_file(file1);
    let matrix2 = load_from_file(file2);
    
    let matrix3 = multiply(&matrix1, &matrix2);

    print_matrix(&matrix3);
}
