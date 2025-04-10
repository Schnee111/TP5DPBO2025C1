public class Mahasiswa {
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String grade;

    public Mahasiswa(String nim, String nama, String jenisKelamin, String grade) {
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.grade = grade;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNim() {
        return this.nim;
    }

    public String getNama() {
        return this.nama;
    }

    public String getJenisKelamin() {
        return this.jenisKelamin;
    }
    
    public String getGrade() {
        return this.grade;
    }
}
