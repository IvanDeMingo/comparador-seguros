package pae.seguros.consultar_seguros;

/**
 * Created by alldocube on 12/01/2018.
 */


public class SeguroItem
{
    public String compania;
    public int precio;
    public int photoId;

    SeguroItem(String compania, int precio, int photoId){
        this.compania = compania;
        this.precio = precio;
        this.photoId = photoId;
    }
}